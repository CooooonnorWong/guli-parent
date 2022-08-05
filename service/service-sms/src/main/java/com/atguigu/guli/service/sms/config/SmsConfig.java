package com.atguigu.guli.service.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Connor
 * @date 2022/8/1
 */
@Data
//@SpringBootConfiguration
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsConfig {
    private String host;
    private String path;
    private String method;
    private String appcode;
    private String tplId;

}
