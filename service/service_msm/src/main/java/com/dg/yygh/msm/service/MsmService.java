package com.dg.yygh.msm.service;

/**
 * @Author: DG
 * @Date: 2021/9/22 20:40
 * @Description:
 */
public interface MsmService {
    // 发送短信验证码
    boolean send(String phone, String code);
}
