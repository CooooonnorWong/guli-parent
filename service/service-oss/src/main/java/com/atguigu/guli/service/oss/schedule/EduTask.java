package com.atguigu.guli.service.oss.schedule;

import com.atguigu.guli.service.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Connor
 * @date 2022/7/25
 */
@Component
@Slf4j
public class EduTask {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OssService ossService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void deleteAvatar() {
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        if (ops.size() == 0) {
            return;
        }
        ops.entries().forEach((path, module) -> {
            ossService.delete(path, module);
            ops.delete(path);
        });
    }
}
