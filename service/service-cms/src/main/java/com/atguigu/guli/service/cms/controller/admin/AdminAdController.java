package com.atguigu.guli.service.cms.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * @author Connor
 * @date 2022/8/1
 */
@RestController
@RequestMapping("/admin/cms/ad")
@Slf4j

@Api(tags = "广告管理模块")
public class AdminAdController {
    @Autowired
    AdService adService;

    @GetMapping
    @ApiOperation("查询所有")
    public R queryAll() {
        return R.ok().data("items", adService.list(new LambdaQueryWrapper<Ad>()
                .orderByDesc(Ad::getSort)));
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询")
    public R getById(@PathVariable("id") String id) {
        return R.ok().data("item", adService.getById(id));
    }

    @PutMapping
    @ApiOperation("更新ad")
    @CacheEvict(value = "ads", key = "'cache'")
    public R updateById(@RequestBody Ad ad) {
        adService.updateById(ad);
        return R.ok();
    }

    @PostMapping
    @ApiOperation("新增ad")
    @CacheEvict(value = "ads", key = "'cache'")
    public R save(@RequestBody Ad ad) {
        adService.save(ad);
        return R.ok();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除ad")
    @CacheEvict(value = "ads", key = "'cache'")
    public R deleteById(@PathVariable("id") String id) {
        adService.removeById(id);
        return R.ok();
    }
}
