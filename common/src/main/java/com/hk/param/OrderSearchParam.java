package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/27 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderSearchParam extends PageBaseVO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "套餐名")
    private String planName;

    @Schema(description = "开始时间")
    private Date startDate;

    @Schema(description = "结束时间")
    private Date endDate;


}
