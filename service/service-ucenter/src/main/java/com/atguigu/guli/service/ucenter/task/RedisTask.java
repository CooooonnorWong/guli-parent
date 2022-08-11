package com.atguigu.guli.service.ucenter.task;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Connor
 * @date 2022/8/8
 */
@Component
public class RedisTask {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Scheduled(cron = "0 5 11 * * ?")
    public void clearLoginId() {
//        redisTemplate.opsForSet().pop(ServiceConsts.DAILY_LOGIN_ID, redisTemplate.opsForSet().size(ServiceConsts.DAILY_LOGIN_ID));
        redisTemplate.delete(ServiceConsts.DAILY_LOGIN_ID);
    }
}
