package com.atguigu.guli.service.edu;

import com.atguigu.guli.service.base.result.R;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author Connor
 * @date 2022/7/25
 */
@SpringBootTest
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    public void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    @Test
    public void testRedisTemplate() {
        redisTemplate.opsForValue().set("rtObj", R.fail(), 20, TimeUnit.MINUTES);
    }

    @Test
    public void testHash() {
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        System.out.println("ops.size() = " + ops.size());
        ops.entries().forEach((k, v) -> System.out.println("k : " + k + " v : " + v));
        System.out.println("ops.keys() = " + ops.keys());
        System.out.println("ops.values() = " + ops.values());

    }
}
