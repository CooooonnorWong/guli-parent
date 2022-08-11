package com.atguigu.guli.service.vod.task;

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
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 5 11 * * ?")
    public void clearVideoId() {
        redisTemplate.delete(ServiceConsts.DAILY_VIDEO_VIEW_ID);
    }
}
