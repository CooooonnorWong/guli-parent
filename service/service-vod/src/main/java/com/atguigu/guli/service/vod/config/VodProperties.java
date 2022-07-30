package com.atguigu.guli.service.vod.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Connor
 * @date 2022/7/29
 */
@SpringBootConfiguration
@ConfigurationProperties(prefix = "aliyun.vod")
@Data
@EnableSwagger2
public class VodProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String workFlowId;

    private String regionId;

}
