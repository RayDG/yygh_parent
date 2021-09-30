package com.dg.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dg.yygh.common.helper.HttpRequestHelper;
import com.dg.yygh.enums.OrderStatusEnum;
import com.dg.yygh.enums.PaymentStatusEnum;
import com.dg.yygh.enums.PaymentTypeEnum;
import com.dg.yygh.hosp.client.HospitalFeignClient;
import com.dg.yygh.model.order.OrderInfo;
import com.dg.yygh.model.order.PaymentInfo;
import com.dg.yygh.order.mapper.OrderMapper;
import com.dg.yygh.order.mapper.PaymentMapper;
import com.dg.yygh.order.service.OrderService;
import com.dg.yygh.order.service.PaymentService;
import com.dg.yygh.vo.order.SignInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/27 19:46
 * @Description:
 */
@Service
public class PaymentServiceImpl extends
        ServiceImpl<PaymentMapper, PaymentInfo> implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    // 向支付记录里添加信息
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
        // 根据订单id和支付类型，查询支付记录是否存在相同订单
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderId, orderInfo.getId());
        wrapper.eq(PaymentInfo::getPaymentType, paymentType);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            return;
        }
        // 添加记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")+"|"+orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        baseMapper.insert(paymentInfo);

    }

    // 更新订单状态
    @Override
    public void paySuccess(String out_trade_no, Map<String, String> resultMap) {
        // 1.根据订单编号获取支付记录
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOutTradeNo, out_trade_no);
        wrapper.eq(PaymentInfo::getPaymentType, PaymentTypeEnum.WEIXIN.getStatus());
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);

        // 2.更新支付记录信息
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfo);

        // 3.根据订单号得到订单信息
        // 4.更新订单信息
        OrderInfo orderInfo = orderService.getById(paymentInfo.getOrderId());
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderService.updateById(orderInfo);

        // 5.调用医院接口，更新订单支付信息
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);

        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl() + "/order/updatePayStatus");
    }

    // 获取支付记录
    @Override
    public PaymentInfo getPaymentInfo(Long orderId, Integer paymentType) {
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper();
        wrapper.eq(PaymentInfo::getOrderId, orderId);
        wrapper.eq(PaymentInfo::getPaymentType, paymentType);
        return baseMapper.selectOne(wrapper);
    }
}
