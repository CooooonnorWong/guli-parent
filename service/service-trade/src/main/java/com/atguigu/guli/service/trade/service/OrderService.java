package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-04
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     *
     * @param courseId
     * @param memberId
     * @return
     */
    String createOrder(String courseId, String memberId);

    /**
     * 根据课程id和会员id判断是否存在订单且已支付
     *
     * @param courseId
     * @param memberId
     * @return
     */
    boolean isBought(String courseId, String memberId);

    /**
     * 查询订单信息在订单页面回显
     *
     * @param orderId
     * @param memberId
     * @return
     */
    Order getOrder(String orderId, String memberId);

    /**
     * 获取微信支付二维码
     *
     * @param orderId
     * @param request
     * @return
     */
    R getCodeUrl(String orderId, HttpServletRequest request);

    /**
     * 微信支付完成后的结果回调
     *
     * @param request
     * @return
     */
    String callback(HttpServletRequest request);

}
