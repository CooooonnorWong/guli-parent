package com.atguigu.guli.service.sms.service;

/**
 * @author Connor
 * @date 2022/8/1
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param mobile
     */
    void sendMsg(String mobile);
}
