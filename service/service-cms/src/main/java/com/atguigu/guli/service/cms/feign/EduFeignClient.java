package com.atguigu.guli.service.cms.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Connor
 * @date 2022/8/1
 */
@Service
@FeignClient("guli-edu")
public interface EduFeignClient {
    /**
     * 查询首页前4个热门讲师
     *
     * @return
     */
    @GetMapping("/api/edu/teacher/getHotTeachers")
    R getHotTeachers();

    /**
     * 查询首页前8个热门课程
     *
     * @return
     */
    @GetMapping("/api/edu/course/getHotCourses")
    R getHotCourses();

}