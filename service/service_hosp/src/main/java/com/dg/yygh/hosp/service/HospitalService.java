package com.dg.yygh.hosp.service;

import com.dg.yygh.model.hosp.Hospital;
import com.dg.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/1 14:49
 * @Description:
 */
public interface HospitalService {
    // 上传医院
    void save(Map<String, Object> paramMap);

    // 根据医院编号查询
    Hospital getByHoscode(String hoscode);

    // 医院列表
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
