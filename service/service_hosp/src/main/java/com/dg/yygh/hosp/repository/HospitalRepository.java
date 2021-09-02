package com.dg.yygh.hosp.repository;

import com.dg.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: DG
 * @Date: 2021/9/1 14:47
 * @Description:
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    // 判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);
}
