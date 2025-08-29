package com.hk.service.system.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.common.ErrorCode;
import com.hk.entity.system.CountyEntity;
import com.hk.event.ProvinceUpdateEvent;
import com.hk.exception.BusinessException;
import com.hk.mapper.system.CountyMapper;
import com.hk.service.system.CityService;
import com.hk.service.system.CountyService;
import com.hk.vo.system.CityVO;
import com.hk.vo.system.CountyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 区县表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Service
public class CountyServiceImpl extends ServiceImpl<CountyMapper, CountyEntity> implements CountyService {

    @Autowired
    private CityService cityService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Boolean addCounty(CountyVO countyVO) {
        CityVO cityVO = cityService.selectOne(countyVO.getCityId());
        if (cityVO == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "城市不存在");
        }
        CountyEntity countyEntity = new CountyEntity();
        countyEntity.setCityId(countyVO.getCityId());
        countyEntity.setCountyCode(countyVO.getCountyCode());
        countyEntity.setCountyName(countyVO.getCountyName());
        boolean save = this.save(countyEntity);
        if (save) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityVO.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return save;
    }

    @Override
    public Boolean deleteCounty(Long id) {
        CountyEntity county = this.getById(id);
        if (county == null) return false;
        CityVO cityVO = cityService.selectOne(county.getCityId());
        boolean remove = this.removeById(id);
        if (remove && cityVO != null) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityVO.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return remove;
    }

    @Override
    public Boolean updateCounty(CountyVO countyVO) {
        CityVO cityVO = cityService.selectOne(countyVO.getCityId());
        if (cityVO == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "城市不存在");
        }
        CountyEntity countyEntity = new CountyEntity();
        countyEntity.setId(countyVO.getId());
        countyEntity.setCountyCode(countyVO.getCountyCode());
        countyEntity.setCountyName(countyVO.getCountyName());
        boolean update = this.updateById(countyEntity);
        if (update) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityVO.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return update;
    }

    @Override
    public CountyVO selectCounty(Long id) {
        CountyEntity countyEntity = this.getById(id);
        if (countyEntity == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "区县不存在");
        }
        return CountyVO.convertToVo(countyEntity);
    }
}
