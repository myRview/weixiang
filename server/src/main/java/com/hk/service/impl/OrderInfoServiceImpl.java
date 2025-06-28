package com.hk.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.OrderInfoEntity;
import com.hk.mapper.OrderInfoMapper;
import com.hk.param.OrderSearchParam;
import com.hk.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.vo.order.OrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements OrderInfoService {
    @Override
    public OrderVO getOrderById(Long id) {
        OrderInfoEntity orderInfo = this.getById(id);
        if (orderInfo != null) {
            return OrderVO.converter(orderInfo);
        }
        return null;
    }

    @Override
    public Page<OrderVO> selectOrderPage(OrderSearchParam searchParam) {
        LambdaQueryWrapper<OrderInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(searchParam.getUserId()!=null,OrderInfoEntity::getUserId, searchParam.getUserId());
        queryWrapper.eq(searchParam.getStatus()!=null,OrderInfoEntity::getStatus, searchParam.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(searchParam.getOrderNo()),OrderInfoEntity::getOrderNumber, searchParam.getOrderNo());

        Page<OrderInfoEntity> page = this.page(new Page<>(searchParam.getPageNum(), searchParam.getPageSize()), queryWrapper);
        Page<OrderVO> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtil.isNotEmpty(page.getRecords())){
            pageResult.setRecords(page.getRecords().stream().map(OrderVO::converter).collect(Collectors.toList()));
        }
        return pageResult;
    }
}
