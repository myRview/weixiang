package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/6/27 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderSearchParam extends PageBaseVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单状态")
    private Integer status;
}
