package com.dg.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.dg.yygh.msm.service.MailService;
import com.dg.yygh.msm.util.ConstantPropertiesUtils;
import com.dg.yygh.vo.msm.MsmVo;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    // mq 发送邮箱封装
    @Override
    public boolean send(MsmVo msmVo) {
        if (!StringUtils.isEmpty(msmVo.getPhone())) {
            boolean isSend = this.send(msmVo.getPhone(), msmVo.getParam());
            return isSend;
        }
        return false;
    }

    private boolean send(String phone, Map<String, Object> param) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(ConstantPropertiesUtils.FROM_EMAIL);
        simpleMailMessage.setTo("raydg@qq.com");
        simpleMailMessage.setSubject("医院预约挂号平台订单");
        String jsonString = JSONObject.toJSONString(param);
        simpleMailMessage.setText("手机号：" + phone + "\n" + jsonString);

        try {
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }
}
