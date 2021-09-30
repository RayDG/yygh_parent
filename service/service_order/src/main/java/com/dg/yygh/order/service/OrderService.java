package com.dg.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.order.OrderInfo;
import com.dg.yygh.vo.order.OrderCountQueryVo;
import com.dg.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/26 17:26
 * @Description:
 */
public interface OrderService extends IService<OrderInfo> {

    // 生成挂号订单
    Long saveOrder(String scheduleId, Long patientId);

    // 根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    // 订单列表（条件查询带分页）
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    // 获取订单详情
    Map<String, Object> show(Long id);

    // 取消预约
    Boolean cancelOrder(Long orderId);

    // 就诊通知
    void patientTips();

    // 预约统计
    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
