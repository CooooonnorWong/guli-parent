package com.atguigu.guli.service.vod.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/29
 */
@RestController
@RequestMapping("/admin/vod")
@Slf4j

@Api(tags = "视频管理模块")
public class AdminVodController {
    @Autowired
    private VodService vodService;

    @PostMapping("/upload")
    @ApiOperation("上传视频")
    public R upload(MultipartFile video) {
        String videoId = vodService.upload(video);
        return R.ok()
                .data("videoId", videoId)
                .data("fileName", video.getOriginalFilename())
                .data("size", video.getSize())
                .data("status", "FULL");
    }

    @GetMapping("/getPlayAuth/{videoId}")
    @ApiOperation("获取播放凭证")
    public R getPlayAuth(@PathVariable String videoId) {
        String playAuth = vodService.getPlayAuth(videoId);
        return R.ok().data("playAuth", playAuth);
    }
}
