package com.atguigu.guli.service.sms.service.impl;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.config.SmsConfig;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.utils.FormUtils;
import com.atguigu.guli.service.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Connor
 * @date 2022/8/1
 */
@Service
@EnableConfigurationProperties(SmsConfig.class)
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsConfig smsConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void sendMsg(String mobile) {
        //发送短信
        //1、验证手机号码格式
        if (!FormUtils.isMobile(mobile)) {
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //2、获取验证码频率判断： 2分钟内只能获取一次，一天内只能获取3次
        if (redisTemplate.opsForValue().get(ServiceConsts.SMS_PREFIX_UNAVAILABLE_FOR_MINUTES + mobile) != null) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        Object o = redisTemplate.opsForValue().get(ServiceConsts.SMS_PREFIX_LIMITS_PER_DAY + mobile);
        if (o == null) {
            redisTemplate.opsForValue().set(ServiceConsts.SMS_PREFIX_LIMITS_PER_DAY + mobile, 0, 1, TimeUnit.DAYS);
        } else {
            if (Integer.parseInt(o.toString()) >= ServiceConsts.SMS_MAX_MSG_COUNT) {
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_REACH_LIMIT_CONTROL);
            }
        }
        try {
            String code = RandomUtils.getSixBitRandom();
            //3、生成验证码发送
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "APPCODE " + smsConfig.getAppcode());
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("mobile", mobile);
            querys.put("param", "code:" + code);
            querys.put("tpl_id", ServiceConsts.SMS_TPL_ID);
            Map<String, String> bodys = new HashMap<String, String>();

//            HttpResponse response = HttpUtils.doPost(smsConfig.getHost(), smsConfig.getPath(), smsConfig.getMethod(), headers, querys, bodys);
//            //获取response的body
//            String resJsonStr = EntityUtils.toString(response.getEntity());
//            log.info(resJsonStr);
//            Map map = new Gson().fromJson(resJsonStr, Map.class);
//            if (!ServiceConsts.SMS_RESPONSE_CODE_VALUE_SUCCESS.equals(map.get(ServiceConsts.SMS_RESPONSE_CODE_KEY))) {
//                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
//            }

            //短信发送成功
            //4、缓存验证码到redis中 15分钟
            redisTemplate.opsForValue().set(ServiceConsts.SMS_PREFIX_CODE + mobile, code, 15, TimeUnit.MINUTES);
            log.info("code:" + code);
            //5、更新手机号码获取验证码的 次数频率
            //2分钟内只能获取一次验证码：redis中只要存一个mobile作为key的数据 过期时间2分钟
            redisTemplate.opsForValue().set(ServiceConsts.SMS_PREFIX_UNAVAILABLE_FOR_MINUTES + mobile, 1, 2, TimeUnit.MINUTES);
            //1天内只能获取3次验证码:   incr k ;  如果k存在，在它的值基础上+1，如果k不存在，默认使用0+1
            //验证码发送成功：在之前获取验证码次数基础上+1
            redisTemplate.opsForValue().increment(ServiceConsts.SMS_PREFIX_LIMITS_PER_DAY + mobile);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR, e);
        }

    }
}
