package com.dg.yygh.hosp.controller.api;

import com.dg.yygh.common.exception.YyghException;
import com.dg.yygh.common.helper.HttpRequestHelper;
import com.dg.yygh.common.result.Result;
import com.dg.yygh.common.result.ResultCodeEnum;
import com.dg.yygh.common.util.MD5;
import com.dg.yygh.hosp.service.DepartmentService;
import com.dg.yygh.hosp.service.HospitalService;
import com.dg.yygh.hosp.service.HospitalSetService;
import com.dg.yygh.hosp.service.ScheduleService;
import com.dg.yygh.model.hosp.Department;
import com.dg.yygh.model.hosp.Hospital;
import com.dg.yygh.model.hosp.HospitalSet;
import com.dg.yygh.model.hosp.Schedule;
import com.dg.yygh.vo.hosp.DepartmentQueryVo;
import com.dg.yygh.vo.hosp.DepartmentVo;
import com.dg.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author: DG
 * @Date: 2021/9/1 14:52
 * @Description:
 */
@RestController
@RequestMapping("/api/hosp")
@Api(tags = "医院开放接口")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    // 删除排班
    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request) {
        // 获取传递过来的排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 获取医院编号和排班编号
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        // TODO 签名
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }

    // 查询排班接口
    @ApiOperation(value = "查询排班列表")
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        // 获取传递过来的排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");

        // 科室编号
        String depcode = (String) paramMap.get("depcode");

        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        // TODO 签名校验

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        // 调用Service方法
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);

        return Result.ok(pageModel);
    }

    // 上传排班接口
    @ApiOperation(value = "上传排班列表")
    @PostMapping("saveSchedule")
    public Result saveSchedule (HttpServletRequest request) {
        // 获取传递过来的排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // TODO 签名校验
        scheduleService.save(paramMap);
        return Result.ok();
    }

    // 删除科室接口
    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        // 获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 医院编号 科室编号
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        // TODO 签名校验

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    // 查询科室接口
    @ApiOperation(value = "查询科室列表")
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
        // 获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 医院编号
        String hoscode = (String) paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        // TODO 签名校验

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        // 调用Service方法
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);

        return Result.ok(pageModel);
    }

    // 上传科室接口
    @ApiOperation(value = "上传科室列表")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 1.获取医院系统传递过来的签名：签名进行MD5加密
        String hosSign = (String) paramMap.get("sign");

        // 2.根据传递过来的医院编码查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        if (!hosSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(paramMap);
        return Result.ok();
    }

    // 查询医院
    @ApiOperation(value = "查询医院")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 1.获取医院系统传递过来的签名：签名进行MD5加密
        String hosSign = (String) paramMap.get("sign");

        // 2.根据传递过来的医院编码查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        if (!hosSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 调用service方法实现根据医院编号查询
        Hospital hosipital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hosipital);
    }

    // 上传医院接口
    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        // 1.获取医院系统传递过来的签名：签名进行MD5加密
        String hosSign = (String) paramMap.get("sign");

        // 2.根据传递过来的医院编码查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        // 3.把数据库查询签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        if (!hosSign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        // 修正 base64 编码，传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }

        // 调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
