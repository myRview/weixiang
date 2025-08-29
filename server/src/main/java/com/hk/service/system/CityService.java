package com.hk.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.system.CityDataEntity;
import com.hk.vo.system.CityVO;

/**
 * <p>
 * 城市表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
public interface CityService extends IService<CityDataEntity> {

    CityVO selectOne(Long cityId);

    Boolean addCity(CityVO cityVO);

    Boolean deleteCity(Long id);

    Boolean updateCity(CityVO cityVO);

    void deleteByProvinceId(Long provinceId);
}
