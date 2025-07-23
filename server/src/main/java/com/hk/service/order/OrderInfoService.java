package com.hk.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.order.OrderInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.param.OrderSearchParam;
import com.hk.vo.order.OrderStatisticsVO;
import com.hk.vo.order.OrderVO;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.plan.PlanStatisticsVO;

import java.util.List;

/**
* <p>
    * 订单表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface OrderInfoService extends IService<OrderInfoEntity> {

    OrderVO getOrderById(Long id);

    Page<OrderVO> selectOrderPage(OrderSearchParam searchParam);

    OrderVO payPlan(PayPlanVo payPlanVo);

    boolean updateStatus(Long orderId, Integer status);

    Integer getOrderStatusById(Long id);

    List<OrderStatisticsVO> getCount(String date);

    List<PlanStatisticsVO> planCount(String date);
}