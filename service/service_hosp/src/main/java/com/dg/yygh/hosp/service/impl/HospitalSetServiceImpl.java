package com.dg.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.common.exception.YyghException;
import com.dg.yygh.common.result.ResultCodeEnum;
import com.dg.yygh.hosp.mapper.HospitalSetMapper;
import com.dg.yygh.hosp.service.HospitalSetService;
import com.dg.yygh.model.hosp.HospitalSet;
import com.dg.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:04
 * @Description:
 */
@Service
public class HospitalSetServiceImpl extends
        ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    // 根据传递过来的医院编码查询数据库，校验签名
    @Override
    public String getSignKey(String hoscode) {
        LambdaQueryWrapper<HospitalSet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HospitalSet::getHoscode, hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(lambdaQueryWrapper);
        return hospitalSet.getSignKey();
    }

    // 获取医院签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(hospitalSet == null) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }


}
