package com.atguigu.guli.service.edu.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Connor
 * @date 2022/7/25
 */
@RestController
@RequestMapping("/admin/edu/subject")
@Api(tags = "课程管理模块")
@Slf4j
@CrossOrigin
public class AdminSubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping("/import")
    @ApiOperation("导入excel")
    public R importSubjects(MultipartFile file) {
        subjectService.importSubjects(file);
        return R.ok();
    }

    @GetMapping("/getNestedSubjects")
    @ApiOperation("查询课程分类嵌套集合")
    public R getNestedSubjects() {
        List<Subject> subjects = subjectService.getNestedSubjects();
        return R.ok().data("items", subjects);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(("根据id获取subject对象"))
    public R getById(@PathVariable String id) {
        return R.ok().data("item", subjectService.getById(id));
    }

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation("根据id删除")
    public R deleteById(@PathVariable String id) {
        if (subjectService.count(new LambdaQueryWrapper<Subject>().eq(Subject::getParentId, id)) > 0L) {
            return R.fail().message("存在子分类");
        }
        subjectService.removeById(id);
        return R.ok();
    }

    @PostMapping("/save")
    @ApiOperation("新增")
    public R save(@RequestBody Subject subject) {
        subjectService.save(subject);
        return R.ok();
    }

    @PutMapping("/updateById")
    @ApiOperation("根据id更新")
    public R updateById(@RequestBody Subject subject) {
        subjectService.updateById(subject);
        return R.ok();
    }
}
