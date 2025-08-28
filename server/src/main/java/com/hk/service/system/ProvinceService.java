package com.hk.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.system.ProvinceEntity;
import com.hk.vo.system.ProvinceVO;

import java.util.List;

/**
 * <p>
 * 省份表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
public interface ProvinceService extends IService<ProvinceEntity> {

    List<ProvinceVO> selectAll();

    List<ProvinceVO> getAllProvince();

    Boolean addProvince(ProvinceVO provinceVO);

    Boolean deleteProvince(Long id);

    Boolean updateProvince(ProvinceVO provinceVO);

    ProvinceVO selectProvince(Long id);

    ProvinceVO saveToRedis(Long provinceId);

    ProvinceVO selectOne(Long provinceId);
}
