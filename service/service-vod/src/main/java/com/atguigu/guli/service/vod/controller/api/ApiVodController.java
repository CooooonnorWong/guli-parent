package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VodService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Connor
 * @date 2022/7/29
 */
@RestController
@RequestMapping("/api/vod")
@Slf4j

public class ApiVodController {
    @Autowired
    private VodService vodService;

    @GetMapping("/getPlayAuth/{videoId}")
    @ApiOperation("获取播放凭证")
    public R getPlayAuth(@PathVariable String videoId) {
        String playAuth = vodService.getPlayAuth(videoId);
        return R.ok().data("playAuth", playAuth);
    }
}
