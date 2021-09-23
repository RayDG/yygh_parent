package com.dg.yygh.msm.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: DG
 * @Date: 2021/9/20 14:52
 * @Description:
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${spring.mail.username}")
    private String from;

    public static String FROM_EMAIL;

    @Override
    public void afterPropertiesSet() throws Exception {
        FROM_EMAIL = from;
    }
}
