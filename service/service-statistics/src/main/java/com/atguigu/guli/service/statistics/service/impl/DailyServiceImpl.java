package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feign.EduClient;
import com.atguigu.guli.service.statistics.feign.UcenterClient;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-07
 */
@Service
@Slf4j
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private EduClient eduClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void genDaily(String date) {
        R registerCountR = ucenterClient.getDailyRegisterMember(date);
        if (!registerCountR.getSuccess() || registerCountR.getCode() != 20000) {
            throw new GuliException(ResultCodeEnum.SERVICE_ERROR);
        }
        R publishCountR = eduClient.getDailyPublishCount(date);
        if (!publishCountR.getSuccess() || publishCountR.getCode() != 20000) {
            throw new GuliException(ResultCodeEnum.SERVICE_ERROR);
        }
        Integer registerCount = (Integer) registerCountR.getData().get("item");
        Integer publishCount = (Integer) publishCountR.getData().get("item");
        Long loginCount = redisTemplate.opsForSet().size(ServiceConsts.DAILY_LOGIN_ID);
        Long videoViewCount = redisTemplate.opsForList().size(ServiceConsts.DAILY_VIDEO_VIEW_ID);

        Daily daily = new Daily();
        daily.setDateCalculated(date);
        daily.setRegisterNum(registerCount);
        daily.setCourseNum(publishCount);
        daily.setLoginNum(Math.toIntExact(loginCount));
        daily.setVideoViewNum(Math.toIntExact(videoViewCount));
        this.save(daily);
        log.info("date: {}", date);
        log.info("registerCount: {}", registerCount);
        log.info("publishCount: {}", publishCount);
        log.info("loginCount: {}", loginCount);
        log.info("videoViewCount: {}", videoViewCount);

    }

    @Override
    public Map<String, Object> getStatistics(String begin, String end) {
        List<Daily> dailies = this.list(new LambdaQueryWrapper<Daily>()
                .ge(Daily::getDateCalculated, begin)
                .le(Daily::getDateCalculated, end)
                .orderByAsc(Daily::getDateCalculated));

        //解析数据：
        //日期集合
        List<String> date = dailies.stream().map(Daily::getDateCalculated).collect(Collectors.toList());
        List<Integer> registerNums = dailies.stream().map(Daily::getRegisterNum).collect(Collectors.toList());
        List<Integer> courseNums = dailies.stream().map(Daily::getCourseNum).collect(Collectors.toList());
        List<Integer> loginNums = dailies.stream().map(Daily::getLoginNum).collect(Collectors.toList());
        List<Integer> videViewNums = dailies.stream().map(Daily::getVideoViewNum).collect(Collectors.toList());

        Map<String,Object> map = new HashMap<>();
        map.put("date",date);
        map.put("registerNums",registerNums);
        map.put("courseNums",courseNums);
        map.put("loginNums",loginNums);
        map.put("videViewNums",videViewNums);

        return map;
    }
}
