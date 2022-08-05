package com.atguigu.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Connor
 * @date 2022/8/2
 */
@SpringBootApplication
@ComponentScan("com.atguigu.guli.service")
@EnableDiscoveryClient
@EnableCaching
public class ServiceUcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUcenterApplication.class, args);
    }
}
