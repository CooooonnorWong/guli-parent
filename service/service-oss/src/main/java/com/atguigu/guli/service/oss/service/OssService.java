package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/22
 */
public interface OssService {
    /**
     * 上传
     *
     * @param file   文件
     * @param module bucket根目录下的文件夹
     * @return OSS对象存储中该文件的路径
     */
    String upload(MultipartFile file, String module);

    /**
     * 删除
     * @param path 文件路径
     * @param module bucket根目录下的文件夹
     */
    void delete(String path, String module);
}
