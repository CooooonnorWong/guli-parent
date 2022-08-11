package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Connor
 * @date 2022/8/8
 */
@FeignClient("guli-edu")
public interface EduClient {
    /**
     * 查询每日课程发布数量
     *
     * @param date
     * @return
     */
    @GetMapping("/admin/edu/course/getDailyPublishCount/{date}")
    R getDailyPublishCount(@PathVariable String date);
}
