package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Connor
 * @date 2022/8/1
 */
@RestController
@RequestMapping("/api/sms")

@Api(tags = "短信模块")
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/sendMsg/{mobile}")
    @ApiOperation("发送短信")
    public R sendMsg(@PathVariable String mobile) {
        smsService.sendMsg(mobile);
        return R.ok();
    }
}
