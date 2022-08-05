package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public void queryPageByCondition(Page<Teacher> page, TeacherQuery teacherQuery) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Teacher::getSort);
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.likeRight(Teacher::getName, name);
        }
        if (level != null) {
            queryWrapper.eq(Teacher::getLevel, level);
        }
        if (!StringUtils.isEmpty(joinDateBegin)) {
            queryWrapper.ge(Teacher::getJoinDate, joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)) {
            queryWrapper.le(Teacher::getJoinDate, joinDateEnd);
        }
        this.page(page, queryWrapper);
    }

    @Override
    public List<Teacher> getHotTeachers() {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Teacher::getId, Teacher::getName, Teacher::getAvatar)
                .orderByDesc(Teacher::getSort)
                .last("LIMIT 0,4");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public String getTeacherName(String teacherId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Teacher>()
                        .eq(Teacher::getId, teacherId)
                        .select(Teacher::getName))
                .getName();
    }

}
