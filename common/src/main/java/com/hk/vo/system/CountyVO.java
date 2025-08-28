package com.hk.vo.system;

import com.hk.entity.system.CountyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 区县表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CountyVO implements Serializable {


    private static final long serialVersionUID = 6685386973727694958L;

    private Long id;

    @Schema(description ="区县名称")
    private String countyName;

    @Schema(description ="区县编码(行政区划代码)")
    private String countyCode;

    @Schema(description ="所属城市ID")
    private Long cityId;


    public static CountyVO convertToVo(CountyEntity entity){
        if(entity == null){
            return null;
        }
        CountyVO vo = new CountyVO();
        vo.setId(entity.getId());
        vo.setCountyName(entity.getCountyName());
        vo.setCountyCode(entity.getCountyCode());
        vo.setCityId(entity.getCityId());
        return vo;
    }
}
