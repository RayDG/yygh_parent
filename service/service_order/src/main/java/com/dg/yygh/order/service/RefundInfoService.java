package com.dg.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dg.yygh.model.order.PaymentInfo;
import com.dg.yygh.model.order.RefundInfo;

/**
 * @Author: DG
 * @Date: 2021/9/28 10:54
 * @Description:
 */
public interface RefundInfoService extends IService<RefundInfo> {

    // 保存退款记录
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
