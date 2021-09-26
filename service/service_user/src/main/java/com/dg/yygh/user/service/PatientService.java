package com.dg.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.user.Patient;

import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/23 15:08
 * @Description:
 */
public interface PatientService extends IService<Patient> {
    // 获取就诊人列表
    List<Patient> findAllByUserId(Long userId);

    // 根据id获取就诊人信息
    Patient getPatientById(Long id);
}
