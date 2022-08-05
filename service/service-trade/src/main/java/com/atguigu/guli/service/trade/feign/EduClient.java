package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Connor
 * @date 2022/8/4
 */
@FeignClient("guli-edu")
public interface EduClient {
    /**
     * 远程调用
     * 根据courseId查询部分课程信息
     *
     * @param courseId
     * @return
     */
    @GetMapping("/api/edu/course/getCourseDto/{courseId}")
    R getCourseDto(@PathVariable String courseId);
}
