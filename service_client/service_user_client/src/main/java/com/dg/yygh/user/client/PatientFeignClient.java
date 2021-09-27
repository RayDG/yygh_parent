package com.dg.yygh.user.client;

import com.dg.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: DG
 * @Date: 2021/9/26 17:42
 * @Description:
 */
@FeignClient(name = "service-user")
@Repository
public interface PatientFeignClient {

    // 根据就诊人id获取就诊人
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);
}
