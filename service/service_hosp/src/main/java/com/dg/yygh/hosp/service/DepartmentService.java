package com.dg.yygh.hosp.service;


import com.dg.yygh.model.hosp.Department;
import com.dg.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/1 19:23
 * @Description:
 */
public interface DepartmentService{
    // 上传科室接口
    void save(Map<String, Object> paramMap);

    // 查询科室接口
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    // 删除科室接口
    void remove(String hoscode, String depcode);
}
