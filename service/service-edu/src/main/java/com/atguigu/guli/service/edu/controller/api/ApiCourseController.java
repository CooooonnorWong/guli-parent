package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/api/edu/course")
@Slf4j
@CrossOrigin
@Api(tags = "课程模块")
public class ApiCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/queryCoursesByTeacherId/{teacherId}")
    @ApiOperation("根据讲师id查询课程列表")
    public R queryCoursesByTeacherId(@PathVariable String teacherId) {
        return R.ok().data("items",
                courseService.list(new LambdaQueryWrapper<Course>()
                        .eq(Course::getTeacherId, teacherId)));
    }
}

