package com.dg.yygh.hosp.controller;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.hosp.service.HospitalService;
import com.dg.yygh.model.hosp.Hospital;
import com.dg.yygh.vo.hosp.HospitalQueryVo;
import com.dg.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: DG
 * @Date: 2021/9/2 10:53
 * @Description:
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    // 医院列表
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }
}
