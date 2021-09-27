package com.dg.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.hosp.HospitalSet;
import com.dg.yygh.vo.order.SignInfoVo;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:04
 * @Description:
 */
public interface HospitalSetService extends IService<HospitalSet> {
    // 根据传递过来的医院编码查询数据库，校验签名
    String getSignKey(String hoscode);

    // 获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
