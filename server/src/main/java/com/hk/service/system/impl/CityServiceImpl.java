package com.hk.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.common.ErrorCode;
import com.hk.entity.system.CityDataEntity;
import com.hk.event.ProvinceUpdateEvent;
import com.hk.exception.BusinessException;
import com.hk.mapper.system.CityMapper;
import com.hk.mapper.system.CountyMapper;
import com.hk.service.system.CityService;
import com.hk.service.system.ProvinceService;
import com.hk.vo.system.CityVO;
import com.hk.vo.system.ProvinceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 城市表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, CityDataEntity> implements CityService {

    @Autowired
    private ApplicationContext applicationContext;
    @Lazy
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CountyMapper countyMapper;

    @Override
    public CityVO selectOne(Long cityId) {
        LambdaQueryWrapper<CityDataEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CityDataEntity::getId, cityId);
        queryWrapper.select(CityDataEntity::getId, CityDataEntity::getCityCode, CityDataEntity::getCityName, CityDataEntity::getProvinceId);
        CityDataEntity cityDataEntity = this.getOne(queryWrapper);
        if (cityDataEntity == null) {
            return null;
        }
        return CityVO.convertToVo(cityDataEntity);
    }

    @Override
    public Boolean addCity(CityVO cityVO) {
        ProvinceVO provinceVO = provinceService.selectOne(cityVO.getProvinceId());
        if (provinceVO == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "省份不存在");
        }
        CityDataEntity cityDataEntity = new CityDataEntity();
        cityDataEntity.setCityCode(cityVO.getCityCode());
        cityDataEntity.setCityName(cityVO.getCityName());
        cityDataEntity.setProvinceId(cityVO.getProvinceId());
        boolean save = this.save(cityDataEntity);
        if (save) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityVO.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteCity(Long id) {
        CityDataEntity cityDataEntity = this.getById(id);
        if (cityDataEntity == null) return false;
        boolean remove = this.removeById(id);
        countyMapper.deleteByCityId(Arrays.asList(id));
        if (remove) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityDataEntity.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return remove;
    }

    @Override
    public Boolean updateCity(CityVO cityVO) {
        ProvinceVO provinceVO = provinceService.selectOne(cityVO.getProvinceId());
        if (provinceVO == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "省份不存在");
        }
        CityDataEntity cityDataEntity = new CityDataEntity();
        cityDataEntity.setCityCode(cityVO.getCityCode());
        cityDataEntity.setCityName(cityVO.getCityName());
        cityDataEntity.setProvinceId(cityVO.getProvinceId());
        boolean update = this.updateById(cityDataEntity);
        if (update) {
            ProvinceUpdateEvent event = new ProvinceUpdateEvent(this, cityVO.getProvinceId());
            applicationContext.publishEvent(event);
        }
        return update;
    }

    @Override
    public void deleteByProvinceId(Long provinceId) {
        LambdaQueryWrapper<CityDataEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CityDataEntity::getProvinceId, provinceId);
        queryWrapper.select(CityDataEntity::getId);
        List<CityDataEntity> entities = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(entities)) return;

        List<Long> cityIdList = entities.stream().map(CityDataEntity::getId).collect(Collectors.toList());
        LambdaQueryWrapper<CityDataEntity> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.eq(CityDataEntity::getProvinceId, provinceId);
        boolean remove = this.remove(removeWrapper);
        countyMapper.deleteByCityId(cityIdList);
    }
}
