package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.service.AdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@RestController
@RequestMapping("/admin/cms/ad-type")
@Slf4j

@Api(tags = "广告类型管理模块")
public class AdminAdTypeController {
    @Autowired
    AdTypeService adTypeService;

    @GetMapping
    @ApiOperation("查询所有")
    public R queryAll() {
        return R.ok().data("items", adTypeService.list());
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询")
    public R getById(@PathVariable("id") String id) {
        return R.ok().data("item", adTypeService.getById(id));
    }

    @PutMapping
    @ApiOperation("更新ad")
    public R updateById(@RequestBody AdType adType) {
        adTypeService.updateById(adType);
        return R.ok();
    }

    @PostMapping
    @ApiOperation("新增ad")
    public R save(@RequestBody AdType adType) {
        adTypeService.save(adType);
        return R.ok();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除ad")
    public R deleteById(@PathVariable("id") String id) {
        adTypeService.removeById(id);
        return R.ok();
    }

}

