package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/22
 */
@RestController
@RequestMapping("/admin/oss")
@Slf4j
@CrossOrigin
@Api(tags = "文件管理模块")
public class OssController {
    @Autowired
    private OssService ossService;

    @PostMapping("/upload")
    @ApiOperation("上传")
    public R upload(@ApiParam("上传文件") MultipartFile file,
                    @RequestParam @ApiParam("bucket根目录下的文件夹") String module) {
        String path = ossService.upload(file, module);
        return R.ok().data("path", path);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestParam @ApiParam("文件路径") String path,
                    @RequestParam @ApiParam("bucket根目录下的文件夹") String module) {
        ossService.delete(path, module);
        return R.ok();
    }
}
