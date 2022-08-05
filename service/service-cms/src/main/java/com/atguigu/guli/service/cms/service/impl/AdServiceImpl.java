package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.feign.EduFeignClient;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private EduFeignClient eduFeignClient;
//    @Autowired
//    private RedisTemplate redisTemplate;

    @Override
    @Cacheable(value = "ads", key = "'cache'")
    public Map<String, List> getHotAds() {
//        Object o = redisTemplate.opsForValue().get("ads::cache");
//        if (o != null) {
//            return (Map<String, List>) o;
//        }
        Map<String, List> map = new HashMap<>();
        map.put("banners", this.list());
        R teachers = eduFeignClient.getHotTeachers();
        R courses = eduFeignClient.getHotCourses();
        if (!teachers.getSuccess() || !courses.getSuccess()) {
            throw new GuliException(ResultCodeEnum.SERVICE_ERROR);
        }
        map.put("teachers", (List) teachers.getData().get("items"));
        map.put("courses", (List) courses.getData().get("items"));
//        redisTemplate.opsForValue().set("ads::cache", map, 30, TimeUnit.MINUTES);
        return map;
    }
}
