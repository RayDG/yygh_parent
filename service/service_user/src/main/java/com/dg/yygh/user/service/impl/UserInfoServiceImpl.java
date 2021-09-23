package com.dg.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.common.exception.YyghException;
import com.dg.yygh.common.helper.JwtHelper;
import com.dg.yygh.common.result.ResultCodeEnum;
import com.dg.yygh.model.user.UserInfo;
import com.dg.yygh.user.mapper.UserInfoMapper;
import com.dg.yygh.user.service.UserInfoService;
import com.dg.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/19 20:49
 * @Description:
 */
@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 用户手机号登录接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        // 获取输入的手机号与验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 判断是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        // 判断手机验证码与输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        System.out.println("redisCode:" + redisCode);
        if (!code.equals(redisCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        // 绑定手机号
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if (userInfo != null) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

        if (userInfo == null) {
            // 判断是否为首次登录，根据手机号查询数据库，如不存在数据则为首次登录
            LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserInfo::getPhone, phone);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                // 添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);

                baseMapper.insert(userInfo);
            }
        }

        // 校验是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }


        // 不是首次登录，直接
        // 返回登录信息

        // 返回登录用户

        // 返回token信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();

        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }

        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }

        map.put("name", name);

        // jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);

        return map;
    }

    // 根据openid判断
    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        return userInfo;
    }
}
