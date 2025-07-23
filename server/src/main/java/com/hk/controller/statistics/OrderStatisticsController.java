package com.hk.controller.statistics;

import com.hk.common.ResponseResult;
import com.hk.service.order.OrderInfoService;
import com.hk.vo.order.OrderStatisticsVO;
import com.hk.vo.plan.PlanStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/7/22 9:36
 */
@Tag(name = "订单统计")
@RestController
@RequestMapping("/order/statistics")
public class OrderStatisticsController {

    @Autowired
    private OrderInfoService orderInfoService;

    @PostMapping("/count")
    @Operation(summary = "统计订单数量")
    @PreAuthorize("@ss.hasPermission('/order/statistics/count')")
    public ResponseResult<List<OrderStatisticsVO>> count(String date) {
        List<OrderStatisticsVO>  statisticsVOList= orderInfoService.getCount(date);
        return ResponseResult.success(statisticsVOList);
    }

    @PostMapping("/plan/count")
    @Operation(summary = "统计套餐购买")
    @PreAuthorize("@ss.hasPermission('/order/statistics/plan/count')")
    public ResponseResult<List<PlanStatisticsVO>> planCount(String date) {
        List<PlanStatisticsVO>  statisticsVOList= orderInfoService.planCount(date);
        return ResponseResult.success(statisticsVOList);
    }
}
