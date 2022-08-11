package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Connor
 * @date 2022/8/5
 */
@Controller
@RequestMapping("/api/trade/wx")
public class ApiWxPayController {
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @GetMapping("/auth/getCodeUrl/{orderId}")
    public R getCodeUrl(@PathVariable String orderId, HttpServletRequest request) {
        return orderService.getCodeUrl(orderId, request);
    }

    @PostMapping("/callback")
    public String callback(HttpServletRequest request) {
        return orderService.callback(request);
    }
}
