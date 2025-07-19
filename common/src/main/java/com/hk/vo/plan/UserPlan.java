package com.hk.vo.plan;

import com.hk.entity.plan.UserPlanEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 用户套餐表
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Data
public class UserPlan implements Serializable {

    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "套餐id")
    private Long planId;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "起效日期")
    private LocalDate startDate;

    @Schema(description = "失效日期")
    private LocalDate endDate;

    @Schema(description = "0-失效，1-有效")
    private Integer status;

    public static UserPlan convert(UserPlanEntity userPlanEntity) {
        if (userPlanEntity == null) return null;
        UserPlan userPlan = new UserPlan();
        userPlan.setId(userPlanEntity.getId());
        userPlan.setUserId(userPlanEntity.getUserId());
        userPlan.setPlanId(userPlanEntity.getPlanId());
        userPlan.setOrderId(userPlanEntity.getOrderId());
        userPlan.setStartDate(userPlanEntity.getStartDate());
        userPlan.setEndDate(userPlanEntity.getEndDate());
        userPlan.setStatus(userPlanEntity.getStatus());
        return userPlan;
    }
}