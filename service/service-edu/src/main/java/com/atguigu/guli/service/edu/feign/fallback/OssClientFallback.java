package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.feign.OssClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Connor
 * @date 2022/7/25
 */
@Service
@Slf4j
public class OssClientFallback implements OssClient {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R upload(MultipartFile file, String module) {
        return null;
    }

    @Override
    public R delete(String path, String module) {
        log.error("OssClient远程服务调用删除讲师头像失败：地址：{},模块：{}", path, module);
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        ops.put(path, module);
        return R.setResult(ResultCodeEnum.GATEWAY_ERROR);
    }
}
