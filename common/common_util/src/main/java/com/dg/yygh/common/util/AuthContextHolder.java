package com.dg.yygh.common.util;

import com.dg.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: DG
 * @Date: 2021/9/23 11:16
 * @Description: 获取当前用户信息工具类
 */
public class AuthContextHolder  {

    // 获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        // 从header获取token
        String token = request.getHeader("token");
        // jwt从token获取userId
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    // 获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        // 从header获取token
        String token = request.getHeader("token");
        // jwt从token获取userId
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
