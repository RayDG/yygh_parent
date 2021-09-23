package com.dg.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DG
 * @Date: 2021/9/19 20:53
 * @Description:
 */
@Configuration
@MapperScan("com.dg.yygh.user.mapper")
public class UserConfig {
}
