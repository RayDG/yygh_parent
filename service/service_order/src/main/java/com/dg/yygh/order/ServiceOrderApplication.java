package com.dg.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: DG
 * @Date: 2021/9/26 17:17
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.dg"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.dg"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
