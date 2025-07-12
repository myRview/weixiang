package com.hk.vo.plan;

import com.hk.entity.plan.MemberPlanEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/27 11:27
 */
@Data
public class MemberPlanVO implements Serializable {

    @Schema(description = "套餐id")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "有效期(天)")
    private Integer validityDays;

    @Schema(description = "0-禁用，1-启用")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    public static MemberPlanVO convert(MemberPlanEntity memberPlanEntity) {
        MemberPlanVO memberPlanVO = new MemberPlanVO();
        memberPlanVO.setId(memberPlanEntity.getId());
        memberPlanVO.setName(memberPlanEntity.getName());
        memberPlanVO.setPrice(memberPlanEntity.getPrice());
        memberPlanVO.setDescription(memberPlanEntity.getDescription());
        memberPlanVO.setValidityDays(memberPlanEntity.getValidityDays());
        memberPlanVO.setStatus(memberPlanEntity.getStatus());
        memberPlanVO.setCreateTime(memberPlanEntity.getCreateTime());
        return memberPlanVO;
    }
}
