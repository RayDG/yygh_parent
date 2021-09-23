package com.dg.yygh.hosp.controller.api;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.hosp.service.DepartmentService;
import com.dg.yygh.hosp.service.HospitalService;
import com.dg.yygh.hosp.service.HospitalSetService;
import com.dg.yygh.model.hosp.Hospital;
import com.dg.yygh.vo.hosp.DepartmentVo;
import com.dg.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/18 09:26
 * @Description:
 */
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname) {
        List<Hospital> list = hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        List<DepartmentVo> depTree = departmentService.findDepTree(hoscode);
        return Result.ok(depTree);
    }

    @ApiOperation(value = "根据医院编号获取预约挂号详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }
}
