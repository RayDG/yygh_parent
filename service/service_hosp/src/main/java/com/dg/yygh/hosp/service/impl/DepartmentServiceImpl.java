package com.dg.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dg.yygh.hosp.repository.DepartmentRepository;
import com.dg.yygh.hosp.service.DepartmentService;
import com.dg.yygh.model.hosp.Department;
import com.dg.yygh.vo.hosp.DepartmentQueryVo;
import com.dg.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: DG
 * @Date: 2021/9/1 19:24
 * @Description:
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // 上传科室接口
    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(jsonString, Department.class);

        // 根据医院编号与科室编号查询
        Department departmentExist = departmentRepository
                .getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        if (departmentExist != null) {
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            department.setId(departmentExist.getId());
            departmentRepository.save(department);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    // 分页
    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        // 创建Pageable对象，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page - 1, limit);
        // 创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    // 删除科室
    @Override
    public void remove(String hoscode, String depcode) {
        // 查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    // 根据医院编号，查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDepTree(String hoscode) {
        // 创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        // 根据医院编号，查询所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        // 所有科室列表 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);

        // 根据大科室编号 bigcode 分组
        Map<String, List<Department>> departmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        // 遍历 map集合 departmentMap
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            // 大科室编号
            String bigCode = entry.getKey();
            // 大科室编号对应的全局数据
            List<Department> bigDepartmentList = entry.getValue();

            // 封装大科室
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigCode);
            bigDepartmentVo.setDepname(bigDepartmentList.get(0).getBigname());

            // 封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            // 遍历大科室
            for (Department department : bigDepartmentList) {
                DepartmentVo smallDepartment = new DepartmentVo();
                smallDepartment.setDepcode(department.getDepcode());
                smallDepartment.setDepname(department.getDepname());
                // 封装到下级节点集合
                children.add(smallDepartment);
            }
            // 下级节点集合添加到大科室
            bigDepartmentVo.setChildren(children);

            // 大科室添加到result中
            result.add(bigDepartmentVo);
        }

        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode
                (hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }
}
