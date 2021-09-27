package com.dg.yygh.user.api;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.common.util.AuthContextHolder;
import com.dg.yygh.model.user.Patient;
import com.dg.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/23 15:07
 * @Description: 就诊人管理接口
 */
@RestController
@RequestMapping("api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    // 获取就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllByUserId(userId);
        return Result.ok(list);
    }

    // 添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        // 获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    // 根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return Result.ok(patient);
    }

    // 修改就诊人
    @PostMapping("auth/update")
    public Result savePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    // 删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result savePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    // 根据id获取就诊人信息
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return patient;
    }

}
