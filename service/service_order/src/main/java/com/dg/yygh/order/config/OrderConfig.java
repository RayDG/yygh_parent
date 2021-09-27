package com.dg.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DG
 * @Date: 2021/9/26 17:29
 * @Description:
 */
@Configuration
@MapperScan("com.dg.yygh.order.mapper")
public class OrderConfig {
}
