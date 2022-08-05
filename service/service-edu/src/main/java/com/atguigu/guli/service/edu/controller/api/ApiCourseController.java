package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.model.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

@Api(tags = "课程模块")
public class ApiCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public R getCourses(ApiCourseQuery apiCourseQuery) {
        List<Course> list = courseService.getCourses(apiCourseQuery);
        return R.ok().data("items", list);
    }

    @GetMapping("/queryCoursesByTeacherId/{teacherId}")
    @ApiOperation("根据讲师id查询课程列表")
    public R queryCoursesByTeacherId(@PathVariable String teacherId) {
        return R.ok().data("items",
                courseService.list(new LambdaQueryWrapper<Course>()
                        .eq(Course::getTeacherId, teacherId)));
    }

    @GetMapping("/{id}")
    public R getCourseDetailVo(@PathVariable String id) {
        ApiCourseDetailVo courseDetailVo = courseService.getCourseDetailVo(id);
        return R.ok().data("item", courseDetailVo);
    }

    @GetMapping("/getHotCourses")
    public R getHotCourses() {
        return R.ok().data("items", courseService.getHotCourses());
    }

    @GetMapping("/getCourseDto/{courseId}")
    public R getCourseDto(@PathVariable String courseId) {
        CourseDto courseDto = courseService.getCourseDto(courseId);
        return R.ok().data("item", courseDto);
    }
}

