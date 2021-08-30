package com.dg.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Author: DG
 * @Date: 2021/8/28 23:21
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.dg")
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }

}