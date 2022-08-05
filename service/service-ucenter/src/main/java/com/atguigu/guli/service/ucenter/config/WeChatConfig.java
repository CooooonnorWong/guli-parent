package com.atguigu.guli.service.ucenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Connor
 * @date 2022/8/2
 */
@Data
@ConfigurationProperties(prefix = "wx.open")
public class WeChatConfig {
    private String appId;
    private String appSecret;
    private String redirectUri;
    private String qrconnectUrl;
    private String accessTokenUrl;
    private String userinfoUrl;
}
