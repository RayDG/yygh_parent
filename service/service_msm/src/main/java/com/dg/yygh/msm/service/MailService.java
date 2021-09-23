package com.dg.yygh.msm.service;

/**
 * @Author: DG
 * @Date: 2021/9/20 14:59
 * @Description:
 */
public interface MailService {
    boolean send(String phone, String code);
}
