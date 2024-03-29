package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.model.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CourseService extends IService<Course> {
    /**
     * 根据courseId获取AdminCourseInfoVo对象
     *
     * @param id
     * @return
     */
    AdminCourseInfoVo getCourseInfo(String id);

    /**
     * 保存AdminCourseInfoVo对象到数据库中
     *
     * @param adminCourseInfoVo
     * @return
     */
    String saveCourseInfo(AdminCourseInfoVo adminCourseInfoVo);

    /**
     * 根据AdminCourseInfoVo和Course的id更新数据库
     *
     * @param vo
     * @param id
     */
    void updateCourseVoById(AdminCourseInfoVo vo, String id);

    /**
     * 查询CourseItemVo分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize);

    /**
     * 根据id删除edu_course和edu_course_description中的数据
     *
     * @param id
     */
    void removeAllById(String id);

    /**
     * 查询课程发布信息
     *
     * @param id
     * @return
     */
    AdminCourseItemVo getCoursePublishVo(String id);

    /**
     * 获取带搜索条件的课程列表
     *
     * @param apiCourseQuery
     * @return
     */
    List<Course> getCourses(ApiCourseQuery apiCourseQuery);

    /**
     * 查询此id的课程展示信息
     *
     * @param id
     * @return
     */
    ApiCourseDetailVo getCourseDetailVo(String id);

    /**
     * 查询首页前8个热门课程
     *
     * @return
     */
    List<Course> getHotCourses();

    /**
     * 根据courseId查询部分课程信息
     *
     * @param courseId
     * @return
     */
    CourseDto getCourseDto(String courseId);
}
