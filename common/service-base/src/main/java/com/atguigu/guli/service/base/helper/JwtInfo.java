package com.atguigu.guli.service.base.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Connor
 * @date 2022/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtInfo {
    private String id;
    private String nickname;
    private String avatar;
    //权限、角色等
    //不要存敏感信息
}
