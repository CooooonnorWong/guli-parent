package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.entity.query.ApiCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.entity.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.entity.vo.ApiCourseDetailVo;
import com.atguigu.guli.service.edu.mapper.CourseDescriptionMapper;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseDescriptionMapper courseDescriptionMapper;

    @Override
    public AdminCourseInfoVo getCourseInfo(String id) {
        AdminCourseInfoVo adminCourseInfoVo = new AdminCourseInfoVo();
        BeanUtils.copyProperties(getById(id), adminCourseInfoVo);
        adminCourseInfoVo.setDescription(courseDescriptionMapper.selectById(id).getDescription());
        return adminCourseInfoVo;
    }

    @Override
    public String saveCourseInfo(AdminCourseInfoVo adminCourseInfoVo) {
        Course course = new Course();
        BeanUtils.copyProperties(adminCourseInfoVo, course);
        save(course);
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(adminCourseInfoVo.getDescription());
        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    @Override
    public void updateCourseVoById(AdminCourseInfoVo vo, String id) {
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(vo.getDescription());
        courseDescription.setId(id);
        courseDescriptionMapper.updateById(courseDescription);
        Course course = new Course();
        BeanUtils.copyProperties(vo, course);
        course.setId(id);
        updateById(course);
    }

    @Override
    public Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize) {
        Page<AdminCourseItemVo> page = new Page<>(pageNum, pageSize);
        List<AdminCourseItemVo> list = baseMapper.selectCourseItemVoPage(page);
        page.setRecords(list);
        return page;
    }

    @Override
    public void removeAllById(String id) {
        baseMapper.deleteById(id);
        courseDescriptionMapper.deleteById(id);
    }

    @Override
    public AdminCourseItemVo getCoursePublishVo(String id) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("t1.id", id);
        return baseMapper.selectCourseItemVo(queryWrapper);
    }

    @Override
    public List<Course> getCourses(ApiCourseQuery apiCourseQuery) {
        String subjectId = apiCourseQuery.getSubjectId();
        String subjectParentId = apiCourseQuery.getSubjectParentId();
        Integer type = apiCourseQuery.getType();
        Integer orderByColumn = apiCourseQuery.getOrderByColumn();
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(subjectId)) {
            queryWrapper.eq(Course::getSubjectId, subjectId);
        }
        if (StringUtils.isNotEmpty(subjectParentId)) {
            queryWrapper.eq(Course::getSubjectParentId, subjectParentId);
        }
        switch (orderByColumn) {
            case 1:
                if (type == 1) {
                    queryWrapper.orderByAsc(Course::getPublishTime);
                } else {
                    queryWrapper.orderByDesc(Course::getPublishTime);
                }
                break;
            case 2:
                if (type == 1) {
                    queryWrapper.orderByAsc(Course::getPrice);
                } else {
                    queryWrapper.orderByDesc(Course::getPrice);
                }
                break;
            default:
                if (type == 1) {
                    queryWrapper.orderByAsc(Course::getBuyCount);
                } else {
                    queryWrapper.orderByDesc(Course::getBuyCount);
                }
                break;
        }
        queryWrapper.eq(Course::getStatus,"Normal");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ApiCourseDetailVo getCourseDetailVo(String id) {
        return baseMapper.getCourseDetailVo(id);
    }

}
