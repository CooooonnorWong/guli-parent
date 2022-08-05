package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.feign.OssClient;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/admin/edu/teacher")
@Api(tags = "讲师管理模块")
@Slf4j

public class AdminTeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private OssClient ossClient;

    /**
     * 分页查询
     */
    @GetMapping("/queryPage/{pageNum}/{pageSize}")
    @ApiOperation("分页查询讲师")
    public R queryPage(@PathVariable(value = "pageNum", required = false)
                       @ApiParam(value = "页码", required = true, defaultValue = "1")
                               Integer pageNum,
                       @PathVariable(value = "pageSize", required = false)
                       @ApiParam(value = "每页数量", required = true, defaultValue = "5")
                               Integer pageSize,
                       TeacherQuery teacherQuery) {
        Page<Teacher> page = new Page<>(pageNum, pageSize);

//        teacherService.page(page, queryWrapper);
        teacherService.queryPageByCondition(page, teacherQuery);
        return R.ok().data("page", page);
    }

    /**
     * 查询全部
     */
    @GetMapping("/queryAll")
    @ApiOperation("查询全部讲师")
    public R listAll() {
        List<Teacher> list = teacherService.list();
//        int i = 10 / 0;
        //默认日志级别：info
//        log.debug("当前时间:{},日志级别:{}", new Date(), "debug");
//        log.info("当前时间:{},日志级别:{}", new Date(), "info");
//        log.warn("当前时间:{},日志级别:{}", new Date(), "warn");
//        log.error("当前时间:{},日志级别:{}", new Date(), "error");
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    /**
     * 根据id查询讲师
     */
    @GetMapping("/getById/{id}")
    @ApiOperation("根据id查询讲师")
    public R queryById(@ApiParam(value = "讲师id", required = true, defaultValue = "1") @PathVariable("id") String id) {
        return R.ok().data("item", teacherService.getById(id));
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation("根据id删除讲师")
    public R removeById(@ApiParam("讲师id") @PathVariable String id) {
        Teacher teacher = teacherService.getById(id);
        boolean remove = teacherService.removeById(id);
        if (remove && !StringUtils.isEmpty(teacher.getAvatar()) && !"null".equals(teacher.getAvatar())) {
            return ossClient.delete(teacher.getAvatar(), "avatar").message("删除成功");
        }
        return R.ok().message("删除成功");
    }

    @DeleteMapping("/removeBatch")
    @ApiOperation("批量根据id集合删除讲师")
    public R removeByIds(@ApiParam("id集合") @RequestBody List<String> ids) {
        List<Teacher> teachers = teacherService.listByIds(ids);
        teachers.forEach(teacher -> {
            boolean remove = teacherService.removeById(teacher);
            if (remove && !StringUtils.isEmpty(teacher.getAvatar()) && !"null".equals(teacher.getAvatar())) {
                ossClient.delete(teacher.getAvatar(), "avatar");
            }
        });
        return R.ok();
    }

    /**
     * 新增讲师: post方式新增，请求体json的方式传入数据
     * 直接使用对象入参，不用任何注解，表示从请求参数中获取属性值设置给对象，pojo入参
     */
    @ApiOperation("新增讲师")
    @PostMapping("/save")
    public R save(@ApiParam("Teacher对象") @RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return R.ok();

    }

    /*
     * 1、自动填充：每张表都有 create和modified属性 都需要自动填充
     * 2、分页拦截器+乐观锁拦截器
     * 3、mp的日志工具
     */

    /**
     * 更新讲师
     */
    @ApiOperation("更新讲师")
    @PutMapping("/update")
    public R update(@ApiParam("Teacher对象") @RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return R.ok();
    }
}

