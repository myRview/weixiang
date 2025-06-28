package com.hk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.OrderInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.param.OrderSearchParam;
import com.hk.vo.order.OrderVO;

/**
* <p>
    * 订单表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface OrderInfoService extends IService<OrderInfoEntity> {

    OrderVO getOrderById(Long id);

    Page<OrderVO> selectOrderPage(OrderSearchParam searchParam);
}