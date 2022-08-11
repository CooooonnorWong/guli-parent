package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Connor
 * @date 2022/8/8
 */
@FeignClient("guli-ucenter")
public interface UcenterClient {
    /**
     * 查询当天用户注册数量
     *
     * @param date
     * @return
     */
    @GetMapping("/api/ucenter/member/getDailyRegisterMember/{date}")
    R getDailyRegisterMember(@PathVariable String date);
}
