package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/api/edu/teacher")
@Api(tags = "讲师模块")
@Slf4j
@CrossOrigin
public class ApiTeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping
    @ApiOperation("查询所有讲师")
    public R queryAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询讲师")
    public R getById(@PathVariable String id) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getId, id)
                .select(Teacher::getAvatar, Teacher::getName, Teacher::getLevel, Teacher::getCareer, Teacher::getIntro);
        return R.ok().data("item", teacherService.getOne(queryWrapper));
    }

}

