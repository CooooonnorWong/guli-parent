package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 分页查询AdminCourseItemPage
     *
     * @param page
     * @return
     */
    List<AdminCourseItemVo> selectCourseItemVoPage(Page<AdminCourseItemVo> page);

    /**
     * 查询课程发布信息
     *
     * @param queryWrapper
     * @return
     */
    AdminCourseItemVo selectCourseItemVo(@Param(value = "ew") QueryWrapper<Course> queryWrapper);

    /**
     * 查询此id的课程信息
     *
     * @param id
     * @return
     */
    ApiCourseDetailVo getCourseDetailVo(String id);
}
