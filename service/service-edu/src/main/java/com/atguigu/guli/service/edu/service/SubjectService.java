package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 导入excel文件到数据库中
     *
     * @param file
     */
    void importSubjects(MultipartFile file);

    /**
     * 查询课程分类嵌套集合
     *
     * @return
     */
    List<Subject> getNestedSubjects();
}
