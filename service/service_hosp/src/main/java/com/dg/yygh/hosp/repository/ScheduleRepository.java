package com.dg.yygh.hosp.repository;

import com.dg.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/2 09:03
 * @Description:
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    // 上传排班接口
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    // 根据医院编号、科室编号和工作日期，查询排班详细信息
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
