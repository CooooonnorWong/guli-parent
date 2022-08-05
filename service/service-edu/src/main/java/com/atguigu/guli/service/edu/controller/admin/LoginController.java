package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.service.base.result.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Connor
 * @date 2022/7/20
 */
@RestController
@RequestMapping("/user")
@Api(tags = "登录模块")
@Slf4j

public class LoginController {

    /**
     * 登录
     */
    @PostMapping("login")
    public R login() {

        return R.ok().data("token", "admin");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("info")
    public R info() {

        return R.ok()
                .data("roles", "[admin]")
                .data("name", "admin")
                .data("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
    }

    /**
     * 退出
     */
    @PostMapping("logout")
    public R logout() {
        return R.ok();
    }
}