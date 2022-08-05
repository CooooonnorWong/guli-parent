package com.atguigu.guli.service.base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Connor
 * @date 2022/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    /**
     * 会员id
     */
    private String id;
    /**
     * 会员昵称
     */
    private String nickname;
    /**
     * 会员手机
     */
    private String mobile;
}
