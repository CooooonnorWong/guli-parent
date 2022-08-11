package com.atguigu.guli.service.statistics.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-07
 */
@RestController
@RequestMapping("/admin/statistics/daily")
public class AdminDailyController {

    @Autowired
    private DailyService dailyService;


    /**
     * 1、生成指定日期的统计数据
     * 登录数量、注册数量、视频点播数量、课程发布数量：
     * <p>
     * 1.1 注册数量： 只需要查询ucenter用户表 创建日期为指定日期的  就是当天的注册用户
     * <p>
     * 1.2 课程发布数量： 查询课程发布时间等于指定日期的 课程
     * <p>
     * > 埋点：
     * 前后端都可以埋点，一般整合第三方的工具，在代码合适的地方调用第三方sdk的方法采集数据
     * <p>
     * 1.3 登录数量：redis(set:每次登录成功的用户的id存到redis的set中，在用户登录时将用户登录时的时间和ip等信息更新到日志表中)
     * <p>
     * 1.4 视频点播数量：在获取视频播放凭证时  将视频id存到redis的List
     * <p>
     * 第二天凌晨2:00通过定时任务统计数据时 将redis中存的统计数据 持久化到数据库并删除缓存
     */
    @PostMapping("/genDaily/{date}")
    public R genDaily(@PathVariable String date) {
        dailyService.genDaily(date);
        return R.ok();
    }

    @GetMapping("/getStatistics/{begin}/{end}")
    public R getStatistics(@PathVariable String begin, @PathVariable String end) {
        Map<String, Object> map = dailyService.getStatistics(begin, end);
        return R.ok()
                .data("date", map.get("date"))
                .data("courseNums", map.get("courseNums"))
                .data("loginNums", map.get("loginNums"))
                .data("videoViewNums", map.get("videoViewNums"));
    }
}

