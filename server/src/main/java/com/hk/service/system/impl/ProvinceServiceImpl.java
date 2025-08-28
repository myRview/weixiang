package com.hk.service.system.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.entity.system.CityDataEntity;
import com.hk.entity.system.CountyEntity;
import com.hk.entity.system.ProvinceEntity;
import com.hk.mapper.system.ProvinceMapper;
import com.hk.service.system.CityService;
import com.hk.service.system.CountyService;
import com.hk.service.system.ProvinceService;
import com.hk.vo.system.CityVO;
import com.hk.vo.system.CountyVO;
import com.hk.vo.system.ProvinceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 省份表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, ProvinceEntity> implements ProvinceService {

    @Autowired
    private CountyService countyService;
    @Autowired
    private CityService cityService;
    @Autowired
    private RedisService<ProvinceVO> redisService;

    @Override
    public List<ProvinceVO> getAllProvince() {
        Map<Object, Object> objectObjectMap = redisService.entriesHash(this.getHashKey());
        List<ProvinceVO> provinceVOList = objectObjectMap.values().stream().map(object -> (ProvinceVO) object).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(provinceVOList)) {
            provinceVOList = selectAll();
        }
        return provinceVOList;
    }

    @Override
    public synchronized List<ProvinceVO> selectAll() {
        LambdaQueryWrapper<ProvinceEntity> provinceWrapper = new LambdaQueryWrapper<>();
        provinceWrapper.select(ProvinceEntity::getId, ProvinceEntity::getProvinceName, ProvinceEntity::getProvinceCode, ProvinceEntity::getPostCode);
        provinceWrapper.orderByAsc(ProvinceEntity::getId);
        List<ProvinceEntity> provinceList = this.list(provinceWrapper);
        if (CollectionUtil.isEmpty(provinceList)) return null;

        //获取区域数据
        LambdaQueryWrapper<CountyEntity> countryWrapper = new LambdaQueryWrapper<>();
        countryWrapper.select(CountyEntity::getId, CountyEntity::getCountyName, CountyEntity::getCountyCode, CountyEntity::getCityId);
        countryWrapper.orderByAsc(CountyEntity::getId);
        List<CountyEntity> countyList = countyService.list(countryWrapper);
        Map<Long, List<CountyVO>> countyMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(countyList)) {
            Map<Long, List<CountyVO>> map = countyList.stream().map(CountyVO::convertToVo).collect(Collectors.groupingBy(CountyVO::getCityId));
            countyMap.putAll(map);
        }

        //获取城市数据
        LambdaQueryWrapper<CityDataEntity> cityWrapper = new LambdaQueryWrapper<>();
        cityWrapper.select(CityDataEntity::getId, CityDataEntity::getCityName, CityDataEntity::getCityCode, CityDataEntity::getProvinceId);
        cityWrapper.orderByAsc(CityDataEntity::getId);
        List<CityDataEntity> cityList = cityService.list(cityWrapper);
        Map<Long, List<CityVO>> cityMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(cityList)) {
            Map<Long, List<CityVO>> map = cityList.stream().map(CityVO::convertToVo).collect(Collectors.groupingBy(CityVO::getProvinceId));
            cityMap.putAll(map);
        }

        List<ProvinceVO> provinceVOList = provinceList.stream().map(provinceEntity -> {
            ProvinceVO provinceVO = ProvinceVO.convertToVo(provinceEntity);
            if (provinceVO != null) {
                List<CityVO> cityVOS = cityMap.get(provinceVO.getId());
                if (CollectionUtil.isNotEmpty(cityVOS)) {
                    for (CityVO cityVO : cityVOS) {
                        List<CountyVO> countyVOS = countyMap.getOrDefault(cityVO.getId(), new ArrayList<>());
                        cityVO.setCountyVOS(countyVOS);
                    }
                    provinceVO.setCityVOS(cityVOS);
                }
            }
            return provinceVO;
        }).collect(Collectors.toList());
        this.saveToRedis(provinceVOList);
        return provinceVOList;
    }

    private void saveToRedis(List<ProvinceVO> provinceVOS) {
        if (CollectionUtil.isEmpty(provinceVOS)) return;
        HashMap<String, ProvinceVO> provinceVOHashMap = new HashMap<>();
        for (ProvinceVO provinceVO : provinceVOS) {
            provinceVOHashMap.put(String.valueOf(provinceVO.getId()), provinceVO);
        }
        redisService.putAllHash(this.getHashKey(), provinceVOHashMap);
        redisService.expire(this.getHashKey(), 60 * 60 * 24 * 30);
    }

    @Override
    public Boolean addProvince(ProvinceVO provinceVO) {
        ProvinceEntity province = new ProvinceEntity();
        BeanUtil.copyProperties(provinceVO, province);
        boolean save = this.save(province);
        if (save) {
            saveToRedis(province.getId());
        }
        return save;
    }

    @Override
    public Boolean deleteProvince(Long id) {
        boolean remove = this.removeById(id);
        if (remove) {
            redisService.deleteHash(this.getHashKey(), String.valueOf(id));
        }
        return remove;

    }

    @Override
    public Boolean updateProvince(ProvinceVO provinceVO) {
        ProvinceEntity province = new ProvinceEntity();
        BeanUtil.copyProperties(provinceVO, province);
        boolean update = this.updateById(province);
        if (update) {
            saveToRedis(province.getId());
        }
        return update;
    }

    @Override
    public ProvinceVO selectProvince(Long id) {
        ProvinceVO provinceVO = redisService.getHash(this.getHashKey(), String.valueOf(id));
        if (provinceVO != null) return provinceVO;
        provinceVO = saveToRedis(id);
        return provinceVO;
    }

    @Override
    public synchronized ProvinceVO saveToRedis(Long provinceId) {
        ProvinceVO provinceVO = this.selectOne(provinceId);
        //获取城市数据
        LambdaQueryWrapper<CityDataEntity> cityWrapper = new LambdaQueryWrapper<>();
        cityWrapper.select(CityDataEntity::getId, CityDataEntity::getCityName, CityDataEntity::getCityCode, CityDataEntity::getProvinceId);
        cityWrapper.eq(CityDataEntity::getProvinceId, provinceId);
        List<CityDataEntity> cityList = cityService.list(cityWrapper);
        if (CollectionUtil.isNotEmpty(cityList)) {
            List<CityVO> cityVOList = cityList.stream().map(CityVO::convertToVo).collect(Collectors.toList());
            List<Long> cityIdList = cityList.stream().map(CityDataEntity::getId).collect(Collectors.toList());

            LambdaQueryWrapper<CountyEntity> countryWrapper = new LambdaQueryWrapper<>();
            countryWrapper.select(CountyEntity::getId, CountyEntity::getCountyName, CountyEntity::getCountyCode, CountyEntity::getCityId);
            countryWrapper.in(CountyEntity::getCityId, cityIdList);
            List<CountyEntity> countyList = countyService.list(countryWrapper);
            if (CollectionUtil.isNotEmpty(countyList)) {
                Map<Long, List<CountyVO>> map = countyList.stream().map(CountyVO::convertToVo).collect(Collectors.groupingBy(CountyVO::getCityId));
                for (CityVO cityVO : cityVOList) {
                    List<CountyVO> countyVOS = map.getOrDefault(cityVO.getId(), new ArrayList<>());
                    cityVO.setCountyVOS(countyVOS);
                }
            }
            provinceVO.setCityVOS(cityVOList);
        }
        redisService.putHash(this.getHashKey(), String.valueOf(provinceId), provinceVO);
        return provinceVO;
    }

    @Override
    public ProvinceVO selectOne(Long provinceId) {
        LambdaQueryWrapper<ProvinceEntity> provinceWrapper = new LambdaQueryWrapper<>();
        provinceWrapper.select(ProvinceEntity::getId, ProvinceEntity::getProvinceName, ProvinceEntity::getProvinceCode, ProvinceEntity::getPostCode);
        provinceWrapper.eq(ProvinceEntity::getId, provinceId);
        provinceWrapper.last(" limit 1");
        ProvinceEntity provinceEntity = this.getOne(provinceWrapper);
        if (provinceEntity == null) return null;
        ProvinceVO provinceVO = ProvinceVO.convertToVo(provinceEntity);
        return provinceVO;
    }

    private String getHashKey() {
        return String.format("%s:%s", BaseConstant.CACHE_PREFIX, "province");
    }
}
