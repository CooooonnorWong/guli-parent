package com.atguigu.guli.service.ucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Connor
 * @date 2022/8/2
 */
@Data
public class ApiMemberVo {
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "验证码")
    private String code;

}
