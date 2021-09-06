package com.dg.yygh.hosp.service;

import com.dg.yygh.model.hosp.Schedule;
import com.dg.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/2 09:04
 * @Description:
 */
public interface ScheduleService {
    // 上传排班接口
    void save(Map<String, Object> paramMap);

    // 查询排班接口
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    // 删除排班接口
    void remove(String hoscode, String hosScheduleId);

    // 根据医院编号 和 科室编号，查询排班规则数据
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
}
