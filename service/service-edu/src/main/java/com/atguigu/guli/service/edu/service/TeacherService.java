package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
public interface TeacherService extends IService<Teacher> {
    /**
     * 条件分页查询
     *
     * @param page         分页
     * @param teacherQuery 查询条件对象
     */
    void queryPageByCondition(Page<Teacher> page, TeacherQuery teacherQuery);

    /**
     * 查询首页前4个热门讲师
     *
     * @return
     */
    List<Teacher> getHotTeachers();

    /**
     * 根据id查询讲师姓名
     *
     * @param teacherId
     * @return
     */
    String getTeacherName(String teacherId);
}
