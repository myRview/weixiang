package com.hk.vo.system;

import com.hk.entity.system.ProvinceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 省份表
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProvinceVO implements Serializable {

    private static final long serialVersionUID = -473535215596210528L;

    private Long id;

    @Schema(description ="省份名称")
    private String provinceName;

    @Schema(description ="省份编码(行政区划代码)")
    private String provinceCode;

    @Schema(description ="省份邮编")
    private String postCode;

    @Schema(description ="城市列表")
    private List<CityVO> cityVOS;

    public static ProvinceVO convertToVo(ProvinceEntity provinceEntity){
        if(provinceEntity == null){
            return null;
        }
        ProvinceVO vo = new ProvinceVO();
        vo.setId(provinceEntity.getId());
        vo.setProvinceName(provinceEntity.getProvinceName());
        vo.setProvinceCode(provinceEntity.getProvinceCode());
        vo.setPostCode(provinceEntity.getPostCode());
        return vo;
    }
}
