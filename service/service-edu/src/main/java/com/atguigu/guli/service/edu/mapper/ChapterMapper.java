package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
public interface ChapterMapper extends BaseMapper<Chapter> {

    /**
     * 根据courseId查询Chapter集合,以及每个chapter的id对应的Video集合
     *
     * @param courseId
     * @return
     */
    List<Chapter> selectChaptersAndVideos(String courseId);
}
