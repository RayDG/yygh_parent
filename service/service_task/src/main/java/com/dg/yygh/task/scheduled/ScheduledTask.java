package com.dg.yygh.task.scheduled;

import com.dg.common.rabbit.constant.MQConst;
import com.dg.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: DG
 * @Date: 2021/9/28 17:36
 * @Description:
 */
@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    // 每天8点执行方法，发送就医提醒
    // cron表达式，设置执行间隔
    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0/30 * * * * ?")
    public void taskPatient() {
        rabbitService.sendMessage(MQConst.EXCHANGE_DIRECT_TASK, MQConst.ROUTING_TASK_8, "");
    }
}
