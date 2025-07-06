package com.hk.entity.plan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户套餐表
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "UserPlanEntity对象", description = "用户套餐表")
@TableName("sys_user_plan")
public class UserPlanEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    private Byte status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}