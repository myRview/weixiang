package com.hk.controller.plan;

import com.hk.common.ResponseResult;
import com.hk.service.order.OrderInfoService;
import com.hk.service.plan.UserPlanService;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.plan.UserPlan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户套餐表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Tag(name = "用户套餐表管理")
@RestController
@RequestMapping("/user/plan")
public class UserPlanController {

    @Autowired
    private UserPlanService userPlanService;

    @Autowired
    private OrderInfoService orderInfoService;

    @PostMapping("/pay/plan")
    @Operation(summary = "用户购买套餐")
    public ResponseResult payPlan(@RequestBody PayPlanVo payPlanVo) {
        return ResponseResult.success(orderInfoService.payPlan(payPlanVo));
    }

    @GetMapping("/user/plan")
    @Operation(summary = "查询用户套餐详情")
    public ResponseResult<UserPlan> getPayPlan(@RequestParam Long userId) {
        UserPlan userPlan = userPlanService.getPayPlan(userId);
        return ResponseResult.success(userPlan);
    }
}