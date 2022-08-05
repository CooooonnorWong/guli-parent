package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Connor
 * @date 2022/8/4
 */
@FeignClient("guli-ucenter")
public interface UcenterClient {
    /**
     * 根据memberId查询部分会员信息
     *
     * @param memberId
     * @return
     */
    @GetMapping("/api/ucenter/member/getMemberDto/{memberId}")
    R getMemberDto(@PathVariable String memberId);
}
