package com.dg.yygh.hosp.repository;

import com.dg.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: DG
 * @Date: 2021/9/1 19:22
 * @Description:
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    // 上传科室接口
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
