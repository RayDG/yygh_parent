package com.dg.yygh.order.receiver;

import com.dg.common.rabbit.constant.MQConst;
import com.dg.yygh.order.service.OrderService;
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
 * @Date: 2021/9/28 17:48
 * @Description:
 */
@Component
public class OrderReceiver {

    @Autowired
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_TASK_8, durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_DIRECT_TASK),
            key = {MQConst.ROUTING_TASK_8}
    ))

    public void patientTips(Message message, Channel channel) throws IOException {
        orderService.patientTips();
    }

}
