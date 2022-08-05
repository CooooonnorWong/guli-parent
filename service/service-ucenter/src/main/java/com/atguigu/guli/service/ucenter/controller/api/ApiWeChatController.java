package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.config.WeChatConfig;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Connor
 * @date 2022/8/3
 */
@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
@Api(tags = "微信登录模块")

@EnableConfigurationProperties(WeChatConfig.class)
public class ApiWeChatController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/login")
    @ApiOperation("获取微信登录二维码")
    public String login(HttpSession session) {
        return memberService.weChatLogin(session);
    }

    @RequestMapping("/callback")
    @ApiOperation("确认登陆后回调获取用户登录信息")
    public String callback(@RequestParam String code, @RequestParam String state, HttpSession session) {
        return memberService.callback(code, state, session);
    }

    @ResponseBody
    @GetMapping("/set")
    public R set(HttpSession session) {
        session.setAttribute("key", "value");
        return R.ok().message("SET");
    }

    @ResponseBody
    @GetMapping("/get")
    public R get(HttpSession session) {
        Object key = session.getAttribute("key");
        System.out.println("session.key = " + key.toString());
        return R.ok().message("GET").data("key", key);
    }

}
