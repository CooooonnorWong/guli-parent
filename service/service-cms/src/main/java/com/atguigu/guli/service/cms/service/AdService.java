package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
public interface AdService extends IService<Ad> {

    /**
     * 获取首页广告信息
     *
     * @return
     */
    Map<String, List> getHotAds();
}
