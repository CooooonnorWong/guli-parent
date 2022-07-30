package com.atguigu.guli.service.vod;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.service.vod.config.VodProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Connor
 * @date 2022/7/29
 */
@SpringBootTest
public class VodTest {
    private String accessKeyId;
    private String accessKeySecret;
    private String workFlowId;
    private String regionId;
    @Autowired
    private VodProperties vodProperties;

    @PostConstruct
    public void init() {
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        workFlowId = vodProperties.getWorkFlowId();
        regionId = vodProperties.getRegionId();
    }

    //获取视频的播放地址
    @Test
    void testGetPlayUrl(){
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId("f4d997dc6c084fceb7ec2868389511f7");
            response =  client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("视频播放地址 = " + playInfo.getPlayURL() + "\n");
            }
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
    }

    /*获取加密视频的播放凭证*/
    @Test
    public void test() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        /*获取播放凭证*/
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId("f4d997dc6c084fceb7ec2868389511f7");
        request.setAuthInfoTimeout(3000L);
        try {
            response = client.getAcsResponse(request);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
