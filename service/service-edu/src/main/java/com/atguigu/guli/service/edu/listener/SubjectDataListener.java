package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.SubjectExcelData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Connor
 * @date 2022/7/25
 */
@Component
public class SubjectDataListener extends AnalysisEventListener<SubjectExcelData> {
    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public void invoke(SubjectExcelData data, AnalysisContext context) {
        String levelOneSubject = data.getLevelOneSubject();
        String levelTwoSubject = data.getLevelTwoSubject();

        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getTitle, levelOneSubject);
        queryWrapper.eq(Subject::getParentId, "0");
        Subject subject = subjectMapper.selectOne(queryWrapper);
        if (subject == null) {
            subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneSubject);
            subject.setSort(0);
            subjectMapper.insert(subject);
        }
        queryWrapper.clear();
        queryWrapper.eq(Subject::getTitle, levelTwoSubject);
        String parentId = subject.getId();
        queryWrapper.eq(Subject::getParentId, parentId);
        subject = subjectMapper.selectOne(queryWrapper);
        if (subject == null) {
            subject = new Subject();
            subject.setSort(0);
            subject.setTitle(levelTwoSubject);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
