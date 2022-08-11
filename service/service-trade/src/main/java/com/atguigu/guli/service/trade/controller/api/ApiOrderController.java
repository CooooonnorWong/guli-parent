package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-04
 */
@RestController
@RequestMapping("/api/trade/order")
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/auth/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        String orderId = orderService.createOrder(courseId, memberId);
        log.info("orderNo = " + orderId);
        return R.ok().data("id", orderId);
    }

    @GetMapping("/auth/isBuy/{courseId}")
    public R isBought(@PathVariable String courseId, HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        boolean b = orderService.isBought(courseId, memberId);
        return R.ok().data("isBuy", b ? "1" : "0");
    }

    @GetMapping("/auth/getOrder/{orderId}")
    public R getOrder(@PathVariable String orderId, HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        Order order = orderService.getOrder(orderId, memberId);
        return R.ok().data("item", order);
    }

    @GetMapping("/auth/getPayStatus/{orderId}")
    public R getPaymentStatus(@PathVariable String orderId) {
        Order order = orderService.getById(orderId);
        return R.ok()
                .data("status", order.getStatus() + "")
                .data("courseId", order.getCourseId());
    }
}

