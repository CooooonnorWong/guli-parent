package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.model.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.ApiMemberVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-02
 */
public interface MemberService extends IService<Member> {

    /**
     * 注册
     *
     * @param apiMemberVo
     */
    void register(ApiMemberVo apiMemberVo);

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @return
     */
    String login(String mobile, String password);

    /**
     * 微信登陆二维码
     *
     * @param session
     * @return
     */
    String weChatLogin(HttpSession session);

    /**
     * 确认登陆后的回调,获取登录信息
     *
     * @param code
     * @param state
     * @param session
     * @return
     */
    String callback(String code, String state, HttpSession session);

    /**
     * 根据id查询部分会员信息
     *
     * @param memberId
     * @return
     */
    MemberDto getMemberDto(String memberId);
}
