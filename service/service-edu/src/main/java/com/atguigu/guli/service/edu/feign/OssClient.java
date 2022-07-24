package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssClientFallback;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/23
 */
@Service
@FeignClient(value = "guli-oss",fallback = OssClientFallback.class)
public interface OssClient {
    /**
     * 调用service-oss中的controller
     *
     * @param file
     * @param module
     * @return
     */
    @PostMapping("/admin/oss/upload")
    @ApiOperation("上传")
    R upload(@ApiParam("上传文件") MultipartFile file,
             @RequestParam @ApiParam("bucket根目录下的文件夹") String module);

    /**
     * 调用service-oss中的controller
     *
     * @param path
     * @param module
     * @return
     */
    @DeleteMapping("/admin/oss/delete")
    @ApiOperation("删除")
    R delete(@RequestParam @ApiParam("文件路径") String path,
             @RequestParam @ApiParam("bucket根目录下的文件夹") String module);
}
