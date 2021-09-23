package com.dg.yygh.msm.controller;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.msm.service.MailService;
import com.dg.yygh.msm.service.MsmService;
import com.dg.yygh.msm.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


/**
 * @Author: DG
 * @Date: 2021/9/20 14:56
 * @Description:
 */
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 发送邮箱验证码
    // TODO 发送手机验证码
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        // 从redis获取验证码，如果获取到返回ok
        // key 手机号 value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }

        // redis获取不到验证码
        // 生成验证码
        code = RandomUtil.getSixBitRandom();
        System.out.println("验证码" + code);
        // TODO 发送手机验证码
        // 发送到邮箱
//        boolean isSend = mailService.send(phone, code);
        boolean isSend = msmService.send(phone, code);
        // 验证码放入redis中，设置有效时间
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 2, TimeUnit.MINUTES);
            return Result.ok();
        }

        return Result.fail().message("短信发送失败！");
    }

}
