package com.dg.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.common.exception.YyghException;
import com.dg.yygh.common.helper.JwtHelper;
import com.dg.yygh.common.result.ResultCodeEnum;
import com.dg.yygh.enums.AuthStatusEnum;
import com.dg.yygh.model.user.Patient;
import com.dg.yygh.model.user.UserInfo;
import com.dg.yygh.user.mapper.UserInfoMapper;
import com.dg.yygh.user.service.PatientService;
import com.dg.yygh.user.service.UserInfoService;
import com.dg.yygh.vo.user.LoginVo;
import com.dg.yygh.vo.user.UserAuthVo;
import com.dg.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private PatientService patientService;

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


        // 不是首次登录，直接登录
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

    // 用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        // 根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        // 设置认证信息
        // 认证人姓名
        userInfo.setName(userAuthVo.getName());
        // 其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        // 进行信息更新
        baseMapper.updateById(userInfo);
    }

    // 用户列表（条件查询带分页）
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        // userInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); // 用户名称
        Integer status = userInfoQueryVo.getStatus(); // 用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); // 认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); // 开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); // 结束时间
        // 对条件值进行非空判断
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(UserInfo::getName, name);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq(UserInfo::getStatus, status);
        }
        if (!StringUtils.isEmpty(authStatus)) {
            wrapper.eq(UserInfo::getAuthStatus, authStatus);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(UserInfo::getCreateTime, createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(UserInfo::getCreateTime, createTimeEnd);
        }
        // 调用mapper的方法
        Page<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        // 编号变成对应值
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });

        return pages;
    }

    // 编号转换成对应值
    private UserInfo packageUserInfo(UserInfo userInfo) {
        // 处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        // 处理用户状态
        String statusString = userInfo.getStatus().intValue() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    // 用户详情
    @Override
    public Map<String, Object> show(Long userId) {
        Map<String, Object> map = new HashMap<>();
        // 根据userId查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo", userInfo);
        // 根据userId查询就诊人信息
        List<Patient> patientList = patientService.findAllByUserId(userId);
        map.put("patientList", patientList);

        return map;
    }

    // 认证审批
    @Override
    public void approval(Long userId, Integer authStatus) {
        if (authStatus.intValue() == 2 || authStatus.intValue() == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }
}
