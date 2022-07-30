package com.atguigu.guli.service.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/29
 */
public interface VodService {
    /**
     * 上传视频
     *
     * @param video
     * @return
     */
    String upload(MultipartFile video);

    /**
     * 获取视频播放凭证
     *
     * @param videoId
     * @return
     */
    String getPlayAuth(String videoId);
}
