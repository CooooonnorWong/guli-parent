package com.atguigu.guli.service.edu.controller.admin;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/admin/edu/course")
@Api(tags = "课程管理模块")
@Slf4j
@CrossOrigin
public class AdminCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/queryPage/{pageNum}/{pageSize}")
    @ApiOperation("分页查询课程信息")
    public R queryPage(@ApiParam("页码") @PathVariable Integer pageNum,
                       @ApiParam("每页大小") @PathVariable Integer pageSize) {
        Page<AdminCourseItemVo> page = courseService.queryCourseItemVoPage(pageNum, pageSize);
        return R.ok().data("page", page);
    }

    @GetMapping("/getCourseInfo/{id}")
    @ApiOperation("查询课程信息")
    public R getCourseInfo(@ApiParam("课程ID") @PathVariable String id) {
        AdminCourseInfoVo vo = courseService.getCourseInfo(id);
        return R.ok().data("item", vo);
    }

    @PostMapping("/saveCourseInfo")
    @ApiOperation("暂存课程信息")
    public R saveCourseInfo(@RequestBody AdminCourseInfoVo adminCourseInfoVo) {
        if (StringUtils.isEmpty(adminCourseInfoVo.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        String id = courseService.saveCourseInfo(adminCourseInfoVo);
        return R.ok().data("id", id);
    }

    @PutMapping("/updateById/{id}")
    @ApiOperation("更新暂存课程信息")
    public R updateById(@PathVariable String id, @RequestBody AdminCourseInfoVo vo) {
        if (StringUtils.isEmpty(vo.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        courseService.updateCourseVoById(vo, id);
        return R.ok();
    }

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation("删除课程信息")
    public R deleteById(@ApiParam("课程ID") @PathVariable String id) {
        courseService.removeAllById(id);
        return R.ok();
    }

    @GetMapping("/getCoursePublish/{id}")
    @ApiOperation("查询课程发布信息")
    public R getCoursePublish(@PathVariable String id) {
        AdminCourseItemVo vo = courseService.getCoursePublishVo(id);
        return R.ok().data("item", vo);
    }

    @PutMapping("/publish/{id}")
    @ApiOperation("发布课程")
    public R publish(@PathVariable String id) {
        courseService.update(new LambdaUpdateWrapper<Course>()
                .set(Course::getStatus , "Normal")
                .set(Course::getPublishTime , new Date())
                .eq(Course::getId , id));
        return R.ok();
    }
}

