package com.dg.yygh.user.api;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.common.util.AuthContextHolder;
import com.dg.yygh.model.user.UserInfo;
import com.dg.yygh.user.service.UserInfoService;
import com.dg.yygh.vo.user.LoginVo;
import com.dg.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/19 20:48
 * @Description:
 */
@Api(value = "用户登录接口")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    // 用户手机号登录接口
    @ApiOperation(value = "用户手机号登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    // 用户认证接口
    @ApiOperation(value = "用户认证")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        // 传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return Result.ok();
    }

    // 获取用户id信息接口
    @ApiOperation(value = "获取用户id信息")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
