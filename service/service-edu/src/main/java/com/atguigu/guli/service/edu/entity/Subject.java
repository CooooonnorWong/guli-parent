package com.atguigu.guli.service.edu.entity;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目
 * </p>
 *
 * @author atguigu
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_subject")
@ApiModel(value = "Subject对象", description = "课程科目")
public class Subject extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类别名称")
    private String title;

    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    /**
     * 添加二级分类集合属性
     * exist = false 表示该字段不是数据库表中的字段
     */
    @TableField(exist = false)
    private List<Subject> children = new ArrayList<>();
}
