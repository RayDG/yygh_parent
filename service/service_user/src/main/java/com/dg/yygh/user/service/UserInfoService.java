package com.dg.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.user.UserInfo;
import com.dg.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/19 20:48
 * @Description:
 */
public interface UserInfoService extends IService<UserInfo> {
    // 用户手机号登录接口
    Map<String, Object> loginUser(LoginVo loginVo);

    // 根据openid判断
    UserInfo selectWxInfoOpenId(String openid);
}
