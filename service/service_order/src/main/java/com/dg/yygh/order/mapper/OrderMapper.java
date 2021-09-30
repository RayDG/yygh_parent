package com.dg.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dg.yygh.model.order.OrderInfo;
import com.dg.yygh.vo.order.OrderCountQueryVo;
import com.dg.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: DG
 * @Date: 2021/9/26 17:28
 * @Description:
 */
public interface OrderMapper extends BaseMapper<OrderInfo> {

    // 查询预约统计数据
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);

}
