package com.dg.yygh.msm.service;

import com.dg.yygh.vo.msm.MsmVo;

/**
 * @Author: DG
 * @Date: 2021/9/20 14:59
 * @Description:
 */
public interface MailService {
    // 发送邮箱验证码
    boolean send(String phone, String code);

    // mq 发送邮件
    boolean send(MsmVo msmVo);
}
