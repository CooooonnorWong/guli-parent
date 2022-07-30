package com.atguigu.guli.service.edu.controller.admin;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/edu/chapter")
@Api(tags = "章节管理模块")
@Slf4j
@CrossOrigin
public class AdminChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/getChaptersAndVideos/{courseId}")
    @ApiOperation("查询章节和视频")
    public R getChaptersAndVideos(@ApiParam("课程ID") @PathVariable String courseId) {
        List<Chapter> chapters = chapterService.getChaptersAndVideos(courseId);
        return R.ok().data("items", chapters);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("查询章节信息")
    public R getById(@PathVariable String id) {
        return R.ok().data("item", chapterService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation("新增章节")
    public R save(@RequestBody Chapter chapter) {
        if (StringUtils.isEmpty(chapter.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        chapterService.save(chapter);
        return R.ok();
    }

    @PutMapping("/updateById")
    @ApiOperation("更新章节")
    public R updateById(@RequestBody Chapter chapter) {
        if (StringUtils.isEmpty(chapter.getTitle())) {
            return R.fail().message("标题不能为空");
        }
        chapterService.updateById(chapter);
        return R.ok();
    }

    @DeleteMapping("/deleteById/{id}")
    @ApiOperation("删除章节")
    public R deleteById(@PathVariable String id) {
        return chapterService.removeByIdAndCheckVideo(id) ? R.ok() : R.fail().message("该章节存在课时!");
    }
}

