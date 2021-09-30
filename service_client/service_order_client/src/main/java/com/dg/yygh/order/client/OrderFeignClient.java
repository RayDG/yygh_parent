package com.dg.yygh.order.client;

import com.dg.yygh.model.user.Patient;
import com.dg.yygh.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/29 19:00
 * @Description:
 */
@FeignClient(name = "service-order")
@Repository
public interface OrderFeignClient {

    // 获取订单统计数量
    @PostMapping("/api/order/orderInfo/inner/getCountMap")
    Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);

}
