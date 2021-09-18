package com.dg.yygh.hosp.service;

import com.dg.yygh.model.hosp.Hospital;
import com.dg.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
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

    // 更新医院上线状态
    void updateStatus(String id, Integer status);

    // 医院详情信息
    Map<String, Object> getHospById(String id);

    // 获取医院名称
    String getHospName(String hoscode);

    // 根据医院名称查询
    List<Hospital> findByHosname(String hosname);
}
