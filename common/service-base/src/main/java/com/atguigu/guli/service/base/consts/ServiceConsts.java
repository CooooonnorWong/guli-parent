package com.atguigu.guli.service.base.consts;

/**
 * @author Connor
 * @date 2022/8/1
 */
public interface ServiceConsts {
    String SMS_RESPONSE_CODE_VALUE_SUCCESS = "00000";
    String SMS_RESPONSE_CODE_KEY = "return_code";
    String SMS_TPL_ID = "TP1711063";

    int SMS_MAX_MSG_COUNT = 3;

    String SMS_PREFIX_CODE = "sms:code:";
    String SMS_PREFIX_UNAVAILABLE_FOR_MINUTES = "sms:per:min:";
    String SMS_PREFIX_LIMITS_PER_DAY = "sms:per:day:";
    /**
     * 盐
     */
    String ENCRYPT_SALT = "@.abc12345~";
    /**
     * 默认头像地址
     */
    String DEFAULT_AVATAR = "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg?imageView2/1/w/80/h/80";
    /**
     * 需要鉴权的路径
     */
    String AUTH_PATH = "/api/**/auth/**";
}
