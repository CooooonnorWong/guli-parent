package com.atguigu.guli.service.cms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.atguigu.guli.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 推荐位
 * </p>
 *
 * @author atguigu
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cms_ad_type")
@ApiModel(value="AdType对象", description="推荐位")
public class AdType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;


}
