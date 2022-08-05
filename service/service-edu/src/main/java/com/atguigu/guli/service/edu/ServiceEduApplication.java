package com.atguigu.guli.service.edu;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Connor
 * @date 2022/7/18
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu.guli.service")
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class ServiceEduApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class, args);
    }

    @Bean
    public IRule myRule() {
        return new RandomRule();
    }

    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }
}
