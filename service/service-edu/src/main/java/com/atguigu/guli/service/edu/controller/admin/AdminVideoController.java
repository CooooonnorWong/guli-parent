package com.atguigu.guli.service.edu.controller.admin;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/admin/edu/video")
@Api(tags = "视频管理模块")
@Slf4j

public class AdminVideoController {

    @Autowired
    private VideoService videoService;

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation("删除课时")
    public R deleteById(@PathVariable String id) {
        videoService.removeById(id);
        return R.ok();
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("查询课时信息")
    public R getById(@PathVariable String id) {
        return R.ok().data("item", videoService.getById(id));
    }

    @PutMapping("/updateById")
    @ApiOperation("更新课时信息")
    public R updateById(@RequestBody Video video) {
        if (StringUtils.isEmpty(video.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        videoService.updateById(video);
        return R.ok();
    }

    @PostMapping("/save")
    @ApiOperation("新增课时信息")
    public R save(@RequestBody Video video) {
        if (StringUtils.isEmpty(video.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        videoService.save(video);
        return R.ok();
    }

}

