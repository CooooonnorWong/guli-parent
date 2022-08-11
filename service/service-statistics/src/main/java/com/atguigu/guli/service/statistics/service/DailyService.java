package com.atguigu.guli.service.statistics.service;

import com.atguigu.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-07
 */
public interface DailyService extends IService<Daily> {

    /**
     * 生成每日统计数据
     *
     * @param date
     */
    void genDaily(String date);

    /**
     * 查询展示数据
     *
     * @param begin
     * @param end
     * @return
     */
    Map<String, Object> getStatistics(String begin, String end);
}
