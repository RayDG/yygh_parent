package com.dg.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.enums.RefundStatusEnum;
import com.dg.yygh.model.order.PaymentInfo;
import com.dg.yygh.model.order.RefundInfo;
import com.dg.yygh.order.mapper.RefundInfoMapper;
import com.dg.yygh.order.service.RefundInfoService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: DG
 * @Date: 2021/9/28 10:55
 * @Description:
 */
@Service
public class RefundInfoServiceImpl
        extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    // 保存退款记录
    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        // 判断是否有重复数据
        LambdaQueryWrapper<RefundInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundInfo::getOrderId, paymentInfo.getOrderId());
        wrapper.eq(RefundInfo::getPaymentType, paymentInfo.getPaymentType());
        RefundInfo refundInfo = baseMapper.selectOne(wrapper);
        if (refundInfo != null) {
            return refundInfo;
        }
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        baseMapper.insert(refundInfo);

        return refundInfo;
    }

}
