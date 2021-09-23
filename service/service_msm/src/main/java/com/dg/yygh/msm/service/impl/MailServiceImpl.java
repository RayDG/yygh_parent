package com.dg.yygh.msm.service.impl;

import com.dg.yygh.msm.service.MailService;
import com.dg.yygh.msm.util.ConstantPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: DG
 * @Date: 2021/9/20 15:01
 * @Description:
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public boolean send(String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(ConstantPropertiesUtils.FROM_EMAIL);
        simpleMailMessage.setTo("raydg@qq.com");
        simpleMailMessage.setSubject("医院预约挂号平台验证码");
        simpleMailMessage.setText("手机号：" + phone + "\n验证码：[" + code + "]");

        try {
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }
}
