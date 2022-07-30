package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 根据courseId查询Chapter集合,以及每个chapter的id对应的Video集合
     *
     * @param courseId
     * @return
     */
    List<Chapter> getChaptersAndVideos(String courseId);

    /**
     * 根据id删除(判断edu_video表是否有chapter_id字段为此id的数据)
     *
     * @param id
     * @return
     */
    boolean removeByIdAndCheckVideo(String id);
}
