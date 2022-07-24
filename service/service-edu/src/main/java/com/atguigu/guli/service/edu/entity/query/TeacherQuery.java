package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Connor
 * @date 2022/7/20
 */
@Data
@ApiModel("讲师查询条件对象")
public class TeacherQuery {
    @ApiModelProperty("讲师姓名")
    private String name;
    @ApiModelProperty("头衔")
    private Integer level;
    @ApiModelProperty("最早入职时间")
    private String joinDateBegin;
    @ApiModelProperty("最晚入职时间")
    private String joinDateEnd;
}
