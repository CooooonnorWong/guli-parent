package com.atguigu.guli.service.base.result;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Connor
 * @date 2022/7/18
 */
@Getter
@ToString
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(true, 20000, "成功"),
    /**
     * 未知错误
     */
    UNKNOWN_REASON(false, 20001, "未知错误"),
    /**
     * sql语法错误
     */
    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    /**
     * json解析异常
     */
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
    /**
     * 参数不正确
     */
    PARAM_ERROR(false, 21003, "参数不正确"),
    /**
     * 文件上传错误
     */
    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
    /**
     * 文件刪除错误
     */
    FILE_DELETE_ERROR(false, 21005, "文件刪除错误"),
    /**
     * Excel数据导入错误
     */
    EXCEL_DATA_IMPORT_ERROR(false, 21006, "Excel数据导入错误"),
    /**
     * 视频上传至阿里云失败
     */
    VIDEO_UPLOAD_ALIYUN_ERROR(false, 22001, "视频上传至阿里云失败"),
    /**
     * 视频上传至业务服务器失败
     */
    VIDEO_UPLOAD_TOMCAT_ERROR(false, 22002, "视频上传至业务服务器失败"),
    /**
     * 阿里云视频文件删除失败
     */
    VIDEO_DELETE_ALIYUN_ERROR(false, 22003, "阿里云视频文件删除失败"),
    /**
     * 获取上传地址和凭证失败
     */
    FETCH_VIDEO_UPLOADAUTH_ERROR(false, 22004, "获取上传地址和凭证失败"),
    /**
     * 刷新上传地址和凭证失败
     */
    REFRESH_VIDEO_UPLOADAUTH_ERROR(false, 22005, "刷新上传地址和凭证失败"),
    /**
     * 获取播放凭证失败
     */
    FETCH_PLAYAUTH_ERROR(false, 22006, "获取播放凭证失败"),
    /**
     * URL编码失败
     */
    URL_ENCODE_ERROR(false, 23001, "URL编码失败"),
    /**
     * 非法回调请求
     */
    ILLEGAL_CALLBACK_REQUEST_ERROR(false, 23002, "非法回调请求"),
    /**
     * 获取accessToken失败
     */
    FETCH_ACCESSTOKEN_FAILD(false, 23003, "获取accessToken失败"),
    /**
     * 获取用户信息失败
     */
    FETCH_USERINFO_ERROR(false, 23004, "获取用户信息失败"),
    /**
     * 登录失败
     */
    LOGIN_ERROR(false, 23005, "登录失败"),
    /**
     * 评论内容必须填写
     */
    COMMENT_EMPTY(false, 24006, "评论内容必须填写"),
    /**
     * 支付中
     */
    PAY_RUN(false, 25000, "支付中"),
    /**
     * 统一下单错误
     */
    PAY_UNIFIEDORDER_ERROR(false, 25001, "统一下单错误"),
    /**
     * 查询支付结果错误
     */
    PAY_ORDERQUERY_ERROR(false, 25002, "查询支付结果错误"),
    /**
     * 课程已购买
     */
    ORDER_EXIST_ERROR(false, 25003, "课程已购买"),
    /**
     * 服务不能访问
     */
    GATEWAY_ERROR(false, 26000, "服务不能访问"),
    /**
     * 服务不能访问
     */
    SERVICE_ERROR(false, 26500, "服务错误"),
    /**
     * 验证码错误
     */
    CODE_ERROR(false, 28000, "验证码错误"),
    /**
     * 手机号码不正确
     */
    LOGIN_PHONE_ERROR(false, 28009, "手机号码不正确"),
    /**
     * 账号不正确
     */
    LOGIN_MOBILE_ERROR(false, 28001, "账号不正确"),
    /**
     * 密码不正确
     */
    LOGIN_PASSWORD_ERROR(false, 28008, "密码不正确"),
    /**
     * 该用户已被禁用
     */
    LOGIN_DISABLED_ERROR(false, 28002, "该用户已被禁用"),
    /**
     * 手机号已被注册
     */
    REGISTER_MOBLE_ERROR(false, 28003, "手机号已被注册"),
    /**
     * 需要登录
     */
    LOGIN_AUTH(false, 28004, "需要登录"),
    /**
     * 没有权限
     */
    LOGIN_ACL(false, 28005, "没有权限"),
    /**
     * 短信发送失败
     */
    SMS_SEND_ERROR(false, 28006, "短信发送失败"),
    /**
     * 短信发送过于频繁
     */
    SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28007, "短信发送过于频繁"),
    /**
     * 短信服务达到每日上限
     */
    SMS_SEND_ERROR_REACH_LIMIT_CONTROL(false, 28807, "短信服务达到每日上限");

    private Boolean success;
    private Integer code;
    private String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
