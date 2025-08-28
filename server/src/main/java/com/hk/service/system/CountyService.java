package com.hk.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.system.CountyEntity;
import com.hk.vo.system.CountyVO;

/**
 * <p>
 * 区县表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
public interface CountyService extends IService<CountyEntity> {

    Boolean addCounty(CountyVO countyVO);

    Boolean deleteCounty(Long id);

    Boolean updateCounty(CountyVO countyVO);

    CountyVO selectCounty(Long id);
}
