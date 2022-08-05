package com.atguigu.guli.infrastructure.gateway.filter;

import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author Connor
 * @date 2022/8/4
 */
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获得请求和响应
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //判断请求是否需要鉴权
        String path = request.getURI().getPath();
        //ant风格比较器比较路径中是否含有"/auth"层
        if (!new AntPathMatcher().match(ServiceConsts.AUTH_PATH, path)) {
            //不符合鉴权路径直接放行
            return chain.filter(exchange);
        }
        //符合路径，继续鉴权
        String token = request.getHeaders().getFirst("token");
        if (JwtHelper.checkToken(token)) {
            //鉴权成功，放行
            return chain.filter(exchange);
        }
        //未登录，token过期,或者token被篡改
        //设置响应头,指定响应体的格式和编码
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        //返回需要登录的R对象
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", ResultCodeEnum.LOGIN_AUTH.getCode());
        jsonObject.addProperty("message", ResultCodeEnum.LOGIN_AUTH.getMessage());
        jsonObject.addProperty("success", ResultCodeEnum.LOGIN_AUTH.getSuccess());
        //将响应报文的字节数组转为缓存数据对象
        DataBuffer data = response.bufferFactory().wrap(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        //将响应数据的缓存对象转为响应报文
        return response.writeWith(Mono.just(data));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
