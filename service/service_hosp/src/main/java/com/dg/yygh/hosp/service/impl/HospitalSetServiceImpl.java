package com.dg.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.hosp.mapper.HospitalSetMapper;
import com.dg.yygh.hosp.service.HospitalSetService;
import com.dg.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:04
 * @Description:
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {
        LambdaQueryWrapper<HospitalSet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HospitalSet::getHoscode, hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(lambdaQueryWrapper);
        return hospitalSet.getSignKey();
    }
}
