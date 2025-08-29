package com.hk.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.entity.system.CountyEntity;

import java.util.List;

/**
 * <p>
 * 区县表 Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
public interface CountyMapper extends BaseMapper<CountyEntity> {

    void deleteByCityId(List<Long> cityIdList);
}
