package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.model.dto.MemberDto;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.config.WeChatConfig;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.ApiMemberVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.utils.FormUtils;
import com.atguigu.guli.service.utils.HttpClientUtils;
import com.atguigu.guli.service.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-02
 */
@Service
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private WeChatConfig weChatConfig;

    @Override
    public void register(ApiMemberVo apiMemberVo) {
        String code = apiMemberVo.getCode();
        String mobile = apiMemberVo.getMobile();
        String nickname = apiMemberVo.getNickname();
        String password = apiMemberVo.getPassword();
        if (StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickname) ||
                !FormUtils.isMobile(mobile)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        Object o = redisTemplate.opsForValue().get(ServiceConsts.SMS_PREFIX_CODE + mobile);
        if (o == null || !code.equals(o.toString())) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }
        if (this.count(new LambdaQueryWrapper<Member>().eq(Member::getMobile, mobile)) > 0L) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(ServiceConsts.ENCRYPT_SALT + MD5.encrypt(password)));
        member.setAvatar(ServiceConsts.DEFAULT_AVATAR);
        this.save(member);
        redisTemplate.delete(ServiceConsts.SMS_PREFIX_CODE + mobile);
    }

    @Override
    public String login(String mobile, String password) {
        if (!FormUtils.isMobile(mobile)) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        Member member = baseMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getMobile, mobile));
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        if (!member.getPassword().equals(MD5.encrypt(ServiceConsts.ENCRYPT_SALT + MD5.encrypt(password)))) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        return JwtHelper.createToken(new JwtInfo(member.getId(), member.getNickname(), member.getAvatar()));
    }

    @Override
    public String weChatLogin(HttpSession session) {
        try {
            String url = "%s?" +
                    "appid=%s" +
                    "&redirect_uri=%s" +
                    "&response_type=code" +
                    "&scope=snsapi_login" +
                    "&state=%s" +
                    "#wechat_redirect";
            String state = UUID.randomUUID().toString().replace("-", "");
            session.setAttribute("state", state);
            url = String.format(url,
                    weChatConfig.getQrconnectUrl(),
                    weChatConfig.getAppId(),
                    URLEncoder.encode(weChatConfig.getRedirectUri(), "UTF-8"),
                    state);
            return "redirect:" + url;
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON, e);
        }

    }

    @Override
    public String callback(String code, String state, HttpSession session) {
        //self_redirect=http://localhost:8160/api/ucenter/wx/callback
        try {
            //验证state
            Object sessionState = session.getAttribute("state");
            if (sessionState == null || !sessionState.equals(state)) {
                throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            session.removeAttribute("state");
            //使用code获取wx用户的wx账号信息
            //1、根据code获取wx中的accessToken字符串(wx用户授权成功的token)
            String url = weChatConfig.getAccessTokenUrl() + "?" +
                    "appid=" + weChatConfig.getAppId() +
                    "&secret=" + weChatConfig.getAppSecret() +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            HttpClientUtils client = new HttpClientUtils(url);
            client.get();
            //获取请求得到的响应结果
            String content = client.getContent();
            if (StringUtils.isEmpty(content) || content.contains("errcode")) {
                log.error(content);
                throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            Gson gson = new Gson();
            Map map = gson.fromJson(content, Map.class);
            String accessToken = map.get("access_token").toString();
            String openid = map.get("openid").toString();

            // 2、根据openid和accessToken获取wx用户的数据
            url = weChatConfig.getUserinfoUrl() + "?" +
                    "access_token=" + accessToken +
                    "&openid=" + openid;
            client = new HttpClientUtils(url);
            client.get();
            content = client.getContent();
            if (StringUtils.isEmpty(content) || content.contains("errcode")) {
                log.error(content);
                throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
            }
            map = gson.fromJson(content, Map.class);
            //解析出用户的数据
            String nickname = map.get("nickname").toString();
            String sex = map.get("sex").toString();
            String province = map.get("province").toString();
            String city = map.get("city").toString();
            String country = map.get("country").toString();
            String headimgurl = map.get("headimgurl").toString();
            log.info("用户数据：nickname={},sex={},province={},city={},country={},avatar={}",
                    nickname, sex, province, city, country, headimgurl);

            // 持久化微信登陆用户到数据库中
            Member member = this.getOne(new LambdaQueryWrapper<Member>()
                    .eq(Member::getOpenid, openid));
            if (member == null) {
                member = new Member();
                //新增
                member.setAvatar(headimgurl);
                member.setNickname(nickname);
                member.setOpenid(openid);
                this.save(member);
            } else {
                if (member.getDisabled()) {
                    throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
                }
                if (System.currentTimeMillis() - member.getGmtModified().getTime() > 1000 * 60 * 60 * 12) {
                    //更新
                    member.setAvatar(headimgurl);
                    member.setNickname(nickname);
                    member.setGmtCreate(null);
                    member.setGmtModified(null);
                    this.updateById(member);
                }
            }

            //将wx用户的数据创建为jwt token 交给3000前端项目的首页回显
            JwtInfo jwtInfo = new JwtInfo();
            jwtInfo.setAvatar(member.getAvatar());
            jwtInfo.setNickname(member.getNickname());
            jwtInfo.setId(member.getId());
            String token = JwtHelper.createToken(jwtInfo);
            //重定向 让浏览器访问3000项目的首页 并携带token参数
            // wx登录只能在本机测试
            return "redirect:http://localhost:3000?token=" + token;
        } catch (IOException | ParseException e) {
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR, e);
        }

    }

    @Override
    public MemberDto getMemberDto(String memberId) {
        Member member = baseMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getId, memberId)
                .select(Member::getMobile, Member::getNickname));
        return new MemberDto(memberId, member.getNickname(), member.getMobile());
    }

}
