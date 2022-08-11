package com.atguigu.guli.service.trade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Connor
 * @date 2022/8/5
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayConfig {
    /**
     * 公众帐号
     */
    private String appid;
    /**
     * 商户号
     */
    private String mchid;
    /**
     * 密钥
     */
    private String partnerKey;
    /**
     * 支付成功的回调地址
     */
    private String notifyUrl;
}
