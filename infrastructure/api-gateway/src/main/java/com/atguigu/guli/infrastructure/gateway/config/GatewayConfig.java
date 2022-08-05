package com.atguigu.guli.infrastructure.gateway.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author Connor
 * @date 2022/8/4
 */
@SpringBootConfiguration
public class GatewayConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //* 允许所有的客户端跨域访问
        config.addAllowedOrigin("*");
        // 允许携带所有请求头跨域访问
        config.addAllowedHeader("*");
        //允许所有的请求方式跨域访问
        config.addAllowedMethod("*");
        //允许携带cookie跨域访问
        config.setAllowCredentials(true);
        //指定一个路径  并为他配置跨域的参数     /**通配所有路径
        configSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(configSource);
    }
}
