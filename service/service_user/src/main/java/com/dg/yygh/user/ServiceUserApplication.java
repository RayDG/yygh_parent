package com.dg.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: DG
 * @Date: 2021/9/19 20:44
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.dg")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.dg")
public class ServiceUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }

}
