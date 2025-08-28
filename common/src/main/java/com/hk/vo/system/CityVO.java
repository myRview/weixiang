package com.hk.vo.system;

import com.hk.entity.system.CityDataEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 城市表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CityVO implements Serializable {

    private static final long serialVersionUID = 4746403614930954789L;

    private Long id;

    @Schema(description ="城市名称")
    private String cityName;

    @Schema(description ="城市编码(行政区划代码)")
    private String cityCode;

    @Schema(description ="所属省份ID")
    private Long provinceId;

    @Schema(description ="区县列表")
    private List<CountyVO> countyVOS;

    public static CityVO convertToVo(CityDataEntity entity){
        if(entity == null){
            return null;
        }
        CityVO vo = new CityVO();
        vo.setId(entity.getId());
        vo.setCityName(entity.getCityName());
        vo.setCityCode(entity.getCityCode());
        vo.setProvinceId(entity.getProvinceId());
        return vo;
    }

}
