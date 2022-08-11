package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.model.dto.MemberDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.ApiMemberVo;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Connor
 * @date 2022/8/2
 */
@RestController
@RequestMapping("/api/ucenter/member")

@Slf4j
@Api(tags = "登录注册模块")
public class ApiMemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    @ApiOperation("注册")
    public R register(@RequestBody ApiMemberVo apiMemberVo) {
        memberService.register(apiMemberVo);
        return R.ok();
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public R login(@ApiParam("手机号码") @RequestParam String mobile, @ApiParam("密码") @RequestParam String password) {
        String token = memberService.login(mobile, password);
        return R.ok().data("item", token);
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取token中的用户数据")
    public R getUserInfo(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("item", jwtInfo);
    }

    @GetMapping("/getMemberDto/{memberId}")
    public R getMemberDto(@PathVariable String memberId) {
        MemberDto memberDto = memberService.getMemberDto(memberId);
        return R.ok().data("item", memberDto);
    }

    @GetMapping("/getDailyRegisterMember/{date}")
    public R getDailyRegisterMember(@PathVariable String date) {
        long count = memberService.count(new QueryWrapper<Member>()
                .eq("date(gmt_create)", date));
        return R.ok().data("item", count);
    }
}
