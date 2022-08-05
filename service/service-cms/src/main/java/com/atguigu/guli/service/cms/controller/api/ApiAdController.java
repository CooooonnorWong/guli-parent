package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@RestController
@RequestMapping("/api/cms/ad")

@Slf4j
@Api(tags = "广告模块")
public class ApiAdController {
    @Autowired
    private AdService adService;

    @GetMapping
    public R getHotAds() {
        Map<String, List> map = adService.getHotAds();
        return R.ok().data("map", map);
    }
}

