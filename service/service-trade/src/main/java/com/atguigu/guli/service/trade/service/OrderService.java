package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
