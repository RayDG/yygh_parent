package com.dg.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: DG
 * @Date: 2021/8/30 19:41
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.dg"})
public class ServiceCmnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }

}
