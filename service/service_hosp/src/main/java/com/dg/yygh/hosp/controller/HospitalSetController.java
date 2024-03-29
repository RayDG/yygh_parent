package com.dg.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dg.yygh.common.exception.YyghException;
import com.dg.yygh.common.result.Result;
import com.dg.yygh.common.util.MD5;
import com.dg.yygh.hosp.service.HospitalSetService;
import com.dg.yygh.model.hosp.HospitalSet;
import com.dg.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

/**
 * @Author: DG
 * @Date: 2021/8/29 00:08
 * @Description:
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    // http://localhost:8201/admin/hosp/hospitalSet/findAll

    // 1.查询医院设置表所有信息
    @ApiOperation(value = "获取分页列表")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    // 2.逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 3.条件查询带分页
    @ApiOperation(value = "医院设置列表（条件分页查询） ")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  // 接收json数据，required = false即值可为空，请求方式必须为Post
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 1.创建Page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        // 2.构建条件
        LambdaQueryWrapper<HospitalSet> wrapper = new LambdaQueryWrapper<>();
        // 条件空值判断
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like(HospitalSet::getHosname, hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq(HospitalSet::getHoscode, hoscode);
        }
        // 3.调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

        return Result.ok(hospitalSetPage);
    }

    // 4.添加医院设置
    @ApiOperation(value = "添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 1.设置状态 1:可使用 0:不可使用
        hospitalSet.setStatus(1);
        // 2.设置签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        // 3.调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 5.根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    // 6.修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // updateById() ---> 将实体类中的id作为查询条件
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 7.批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("batchRemove")
    // 接收参数示例：[2, 3]
    public Result batchRemoveHospitalSet(@RequestBody List<String> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    // 8.医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        // 1.根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 2.设置状态
        hospitalSet.setStatus(status);
        // 3.调用update方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    // 9.发送签名密钥
    @ApiOperation(value = "发送签名密钥")
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
