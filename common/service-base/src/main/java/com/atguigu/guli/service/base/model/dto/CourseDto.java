package com.atguigu.guli.service.base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Connor
 * @date 2022/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    /**
     * 课程id
     */
    private String id;
    /**
     * 课程名称
     */
    private String title;
    /**
     * 课程封面路径
     */
    private String cover;
    /**
     * 课程讲师姓名
     */
    private String teacherName;
    /**
     * 课程价格(分)
     */
    private BigDecimal price;

}
