package com.dg.yygh.order.api;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.enums.PaymentTypeEnum;
import com.dg.yygh.order.service.PaymentService;
import com.dg.yygh.order.service.WeixinService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: DG
 * @Date: 2021/9/27 19:39
 * @Description:
 */
@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private PaymentService paymentService;

    // 生成微信支付二维码
    @GetMapping("createNative/{orderId}")
    public Result createNative(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        Map map = weixinService.createNative(orderId);
        return Result.ok(map);
    }

    // 查询支付状态
    @GetMapping("queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable("orderId") Long orderId) {
        // 调用微信接口查询支付状态
        Map<String, String> resultMap = weixinService.queryPayStatus(orderId);
        if (resultMap == null) { //出错
            return Result.fail().message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) { //如果成功
            // 更新订单状态
            String out_trade_no = resultMap.get("out_trade_no");
            paymentService.paySuccess(out_trade_no, resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }
}
