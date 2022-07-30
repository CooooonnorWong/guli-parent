package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.SubjectExcelData;
import com.atguigu.guli.service.edu.listener.SubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Resource
    private SubjectDataListener subjectDataListener;
    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public void importSubjects(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream())
                    .head(SubjectExcelData.class)
                    .sheet(0)
                    .registerReadListener(subjectDataListener)
                    .doRead();
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR, e);
        }
    }

    @Override
    public List<Subject> getNestedSubjects() {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        List<Subject> subjects = subjectMapper.selectList(queryWrapper);
        List<Subject> parentSubject = subjects.stream()
                .filter(subject -> "0".equals(subject.getParentId()))
                .collect(Collectors.toList());
        parentSubject.forEach(pSubject -> {
            List<Subject> childSubjects = subjects.stream()
                    .filter(subject -> subject.getParentId().equals(pSubject.getId()))
                    .collect(Collectors.toList());
            pSubject.setChildren(childSubjects);
        });
        return parentSubject;
    }
}
