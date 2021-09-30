package com.dg.yygh.order.service;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/27 19:39
 * @Description:
 */
public interface WeixinService {

    // 生成微信支付二维码
    Map createNative(Long orderId);

    // 调用微信接口查询支付状态
    Map<String, String> queryPayStatus(Long orderId);

    // 退款
    Boolean refund(Long orderId);
}
