package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.model.dto.CourseDto;
import com.atguigu.guli.service.base.model.dto.MemberDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.config.WxPayConfig;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.EduClient;
import com.atguigu.guli.service.trade.feign.UcenterClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.PayLogService;
import com.atguigu.guli.service.utils.HttpClientUtils;
import com.atguigu.guli.service.utils.OrderNoUtils;
import com.atguigu.guli.service.utils.StreamUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-04
 */
@Service
@Slf4j
@EnableConfigurationProperties(WxPayConfig.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private WxPayConfig wxPayConfig;
    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private Gson gson;
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String createOrder(String courseId, String memberId) {
        //先根据courseId和memberId在订单表中查询
        Order order = this.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId));
        if (order != null && order.getStatus() == 1) {
            //订单存在且已付款则抛出已购买异常
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }
        //订单不存在或者未付款，继续
        //分别远程调用查询course表和member表
        R courseDtoR = eduClient.getCourseDto(courseId);
        R memberDtoR = ucenterClient.getMemberDto(memberId);
        if (!memberDtoR.getCode().equals(R.ok().getCode()) || !courseDtoR.getCode().equals(R.ok().getCode())) {
            //查询失败，抛出异常
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        CourseDto courseDto = objectMapper.convertValue(courseDtoR.getData().get("item"), CourseDto.class);
        MemberDto memberDto = objectMapper.convertValue(memberDtoR.getData().get("item"), MemberDto.class);

        if (order == null) {
            order = new Order();
        }
        //2、判断 如果用户已下单但是未支付  更新订单信息为最新的 返回订单id
        order.setStatus(0);
        //订单编号
        order.setOrderNo(OrderNoUtils.getOrderNo());
        //会员数据
        order.setMemberId(memberId);
        order.setNickname(memberDto.getNickname());
        order.setMobile(memberDto.getMobile());
        //订单的课程数据
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        //courseDto.getPrice()单元元，需要转为分
        long price = courseDto.getPrice().multiply(new BigDecimal("100")).longValue();
        order.setTotalFee(price);
        order.setTeacherName(courseDto.getTeacherName());
        order.setCourseId(courseId);
        //保存或者更新到数据库
        if (!StringUtils.isEmpty(order.getId())) {
            //订单id存在更新
            this.updateById(order);
        } else {
            //新增
            this.save(order);
        }
        //返回订单id
        return order.getId();
    }

    @Override
    public boolean isBought(String courseId, String memberId) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId)
                .eq(Order::getStatus, 1));
        return order != null;
    }

    @Override
    public Order getOrder(String orderId, String memberId) {
        return this.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getMemberId, memberId));
    }

    @Override
    public R getCodeUrl(String orderId, HttpServletRequest request) {
        try {
            Order order = this.getById(orderId);
            HttpClientUtils client = new HttpClientUtils(ServiceConsts.UNIFIED_ORDER_URL);

            // 为请求准备参数： 由于wx支付v2版本 使用xml传递数据，
            // xml格式不便于封装 可以使用wx提供的工具类将map转为xml文件
            Map<String, String> map = new HashMap<>();
            //公众账号ID
            map.put("appid", wxPayConfig.getAppid());
            //商户号
            map.put("mch_id", wxPayConfig.getMchid());
            //随机字符串
            String nonceStr = WXPayUtil.generateNonceStr();
            map.put("nonce_str", nonceStr);
            //签名：可以为数据生成签名 防止数据被篡改
//        map.put("sign", "");
            //商品描述
            map.put("body", order.getCourseTitle());
            //商户订单号
            map.put("out_trade_no", order.getOrderNo());
            //标价金额(分)
            map.put("total_fee", order.getTotalFee().toString());
            //终端IP:用户客户端的ip地址
            //需要使用公网ip 或者 内网穿透工具
            map.put("spbill_create_ip", request.getRemoteHost());
            //通知的回调地址
            //回调接口地址:wx平台来访问，如果接口地址使用localhost或者局域网ip wx一定不能访问
            map.put("notify_url", wxPayConfig.getNotifyUrl());
            //交易类型
            map.put("trade_type", "NATIVE");
            //使用秘钥对上面的map集合处理生成签名 并将map和签名的结果 一起转为一个xml文档字符串
            String signedXml = WXPayUtil.generateSignedXml(map, wxPayConfig.getPartnerKey());
            log.info("params: {}", signedXml);
            //设置请求参数
            client.setXmlParam(signedXml);
            //请求微信服务器
            client.post();
            //获取响应结果
            String content = client.getContent();
            if (StringUtils.isEmpty(content)) {
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
            if (!WXPayUtil.isSignatureValid(content, wxPayConfig.getPartnerKey())) {
                throw new GuliException(ResultCodeEnum.PAY_WX_SIGUNATURE_VALID_ERROR);
            }
            log.info("result: {}", content);
            //Xml格式字符串转换成Map
            Map<String, String> returnedMap = WXPayUtil.xmlToMap(content);
            String returnCode = returnedMap.get("return_code");
            String returnMsg = returnedMap.get("result_code");
            if (StringUtils.isEmpty(returnCode) ||
                    StringUtils.isEmpty(returnMsg) ||
                    !"SUCCESS".equals(returnCode) ||
                    !"SUCCESS".equals(returnMsg)) {
                log.error("获取wx支付二维码失败：" + content);
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
//            request.getSession().setAttribute("nonceStr", nonceStr);
            log.info("code_url : {}", returnedMap.get("code_url"));
            return R.ok().data("code_url", returnedMap.get("code_url"))
                    .data("out_trade_no", order.getOrderNo())
                    .data("total_fee", order.getTotalFee())
                    .data("courseId", order.getCourseId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR, e);
        }
    }

    @Override
    public String callback(HttpServletRequest request) {
        //给微信应答的map集合
        Map<String, String> replyMap = new HashMap<>();
        replyMap.put("return_code", "FAIL");
        replyMap.put("return_msg", "FAIL");

        try {
            //获取请求体中的输入流
            String xmlStr = StreamUtils.inputStream2String(request.getInputStream(), "UTF-8");
            //1、验证签名
            if (!WXPayUtil.isSignatureValid(xmlStr, wxPayConfig.getPartnerKey())) {
                log.error("签名验证失败：" + xmlStr);
                replyMap.put("return_msg", "签名错误");
                throw new GuliException(ResultCodeEnum.PAY_WX_SIGUNATURE_VALID_ERROR);
            }
            //2、验证处理结果是否成功：
            Map<String, String> xmlMap = WXPayUtil.xmlToMap(xmlStr);
            String resultCode = xmlMap.get("result_code");
            String returnCode = xmlMap.get("return_code");
            if (StringUtils.isEmpty(returnCode) ||
                    StringUtils.isEmpty(resultCode) ||
                    !"SUCCESS".equals(returnCode) ||
                    !"SUCCESS".equals(resultCode)) {
                log.error("支付失败: " + xmlStr);
                replyMap.put("return_msg", "支付失败");
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            //3、验证支付金额和订单实际金额是否一致
            Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, xmlMap.get("out_trade_no")));
            if (order == null ||
                    order.getTotalFee() != Long.parseLong(xmlMap.get("cash_fee"))) {
                replyMap.put("return_msg", "订单不存在或者支付金额和订单金额不一致");
                log.error("订单不存在或者支付金额和订单金额不一致: " + xmlStr);
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            //4、订单支付成功：
            // 更新订单支付状态
            //1 支付成功状态
            order.setStatus(1);
            //支付方式 1微信
            order.setPayType(1);
            this.updateById(order);
            // 保存支付日志
            PayLog payLog = new PayLog();
            payLog.setPayType(1);
            payLog.setTransactionId(xmlMap.get("transaction_id"));
            payLog.setTotalFee(order.getTotalFee());
            payLog.setTradeState("SUCCESS");
            payLog.setPayTime(new Date());
            payLog.setOrderNo(order.getOrderNo());
            payLog.setAttr(xmlStr);
            payLogService.save(payLog);

            //更新课程销量->rabbitmq消息队列
            rabbitTemplate.convertAndSend("guli.order.exchange",
                    "order.pay.ok", gson.toJson(order));

            replyMap.put("return_code", "SUCCESS");
            replyMap.put("return_msg", "SUCCESS");
            return WXPayUtil.mapToXml(replyMap);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            try {
                return WXPayUtil.mapToXml(replyMap);
            } catch (Exception ex) {
                log.error(Arrays.toString(ex.getStackTrace()));
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR, ex);
            }
        }
    }

}
