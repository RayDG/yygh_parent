package com.dg.yygh.hosp.controller;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.hosp.service.DepartmentService;
import com.dg.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/3 10:51
 * @Description:
 */
@Api(tags = "医院科室管理")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    // 根据医院编号，查询医院所有科室列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDepList/{hoscode}")
    public Result getDepList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDepTree(hoscode);
        return Result.ok(list);
    }
}
