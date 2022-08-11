package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.config.VodProperties;
import com.atguigu.guli.service.vod.service.VodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Connor
 * @date 2022/7/29
 */
@Service
@Slf4j
public class VodServiceImpl implements VodService {
    private String accessKeyId;
    private String accessKeySecret;
    private String workFlowId;
    private String regionId;
    @Autowired
    private VodProperties vodProperties;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostConstruct
    public void init() {
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        workFlowId = vodProperties.getWorkFlowId();
        regionId = vodProperties.getRegionId();
    }

    @Override
    public String upload(MultipartFile video) {
        try {
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,
                    accessKeySecret,
                    video.getOriginalFilename(),
                    video.getOriginalFilename(),
                    video.getInputStream());
            /* 工作流ID（可选）*/
            request.setWorkflowId(workFlowId);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //请求视频点播服务的请求ID
            System.out.print("RequestId=" + response.getRequestId() + "\n");
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                return response.getVideoId();
            } else {
                //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                log.error("VideoId=" + response.getVideoId() + " ErrorCode=" + response.getCode() + " ErrorMessage=" + response.getMessage());
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
            }
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR, e);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR, e);
        }
    }

    @Override
    public String getPlayAuth(String videoId) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        /*获取播放凭证*/
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        request.setAuthInfoTimeout(3000L);
        try {
            response = client.getAcsResponse(request);
            //播放凭证
            log.info("PlayAuth = " + response.getPlayAuth());
            //VideoMeta信息
            log.info("VideoMeta.Title = " + response.getVideoMeta().getTitle());
            redisTemplate.opsForList().leftPush(ServiceConsts.DAILY_VIDEO_VIEW_ID, videoId);
            return response.getPlayAuth();
        } catch (Exception e) {
            log.error("ErrorMessage = " + e.getLocalizedMessage());
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR, e);
        }
    }
}
