package com.dg.yygh.msm.receiver;

import com.dg.common.rabbit.constant.MQConst;
import com.dg.yygh.msm.service.MailService;
import com.dg.yygh.vo.msm.MsmVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: DG
 * @Date: 2021/9/26 20:50
 * @Description:
 */
@Component
public class MsmReceiver {

    @Autowired
    private MailService mailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_MSM_ITEM, durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_MSM),
            key = {MQConst.ROUTING_MSM_ITEM}
    ))
    public void send(MsmVo msmVo, Message message, Channel channel) {
        mailService.send(msmVo);
    }
}
