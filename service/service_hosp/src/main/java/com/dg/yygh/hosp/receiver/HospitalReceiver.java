package com.dg.yygh.hosp.receiver;

import com.dg.common.rabbit.constant.MQConst;
import com.dg.common.rabbit.service.RabbitService;
import com.dg.yygh.hosp.service.ScheduleService;
import com.dg.yygh.model.hosp.Schedule;
import com.dg.yygh.vo.msm.MsmVo;
import com.dg.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: DG
 * @Date: 2021/9/27 08:52
 * @Description:
 */
@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_ORDER),
            key = {MQConst.ROUTING_ORDER}
    ))

    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        if(orderMqVo.getAvailableNumber() != null) {
            // 下单成功更新预约数
            Schedule schedule = scheduleService.getScheduleId(orderMqVo.getScheduleId());
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            scheduleService.update(schedule);
        } else {
            // 取消预约更新预约数
            Schedule schedule = scheduleService.getScheduleId(orderMqVo.getScheduleId());
            int availableNumber = schedule.getAvailableNumber().intValue() + 1;
            schedule.setAvailableNumber(availableNumber);
            scheduleService.update(schedule);
        }
        // 发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(msmVo != null) {
            rabbitService.sendMessage(MQConst.EXCHANGE_DIRECT_MSM, MQConst.ROUTING_MSM_ITEM, msmVo);
        }

    }


}
