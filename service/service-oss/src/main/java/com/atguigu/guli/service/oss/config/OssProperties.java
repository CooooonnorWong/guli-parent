package com.atguigu.guli.service.oss.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Connor
 * @date 2022/7/22
 */
@Data
@SpringBootConfiguration
@EnableSwagger2
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    /**
     * Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
     * 自己的桶所在的oss域名
     */
    private String endpoint;
    // 由于阿里云账号能够操作阿里云中所有的内容，建议创建一个子账户并分配阿里云oss操作的权限
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    // 在阿里云oss控制台创建的子账户并分配oss Full权限
    /**
     * Id
     */
    private String accessKeyId;
    /**
     * Password
     */
    private String accessKeySecret;
    /**
     * 填写Bucket名称，例如examplebucket。
     */
    private String bucketName;
}
