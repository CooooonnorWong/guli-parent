package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.model.dto.CourseDto;
import com.atguigu.guli.service.base.model.dto.MemberDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feign.EduClient;
import com.atguigu.guli.service.trade.feign.UcenterClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

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
}
