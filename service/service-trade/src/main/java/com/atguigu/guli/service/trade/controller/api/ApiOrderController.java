package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

