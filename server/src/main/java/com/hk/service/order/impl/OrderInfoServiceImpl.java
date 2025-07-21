package com.hk.service.order.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ErrorCode;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.enums.MessageTypeEnum;
import com.hk.enums.OrderStatusEnum;
import com.hk.exception.BusinessException;
import com.hk.manager.AlipayManager;
import com.hk.mapper.order.OrderInfoMapper;
import com.hk.mq.MessageProducer;
import com.hk.param.OrderSearchParam;
import com.hk.service.plan.MemberPlanService;
import com.hk.service.order.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.service.user.UserService;
import com.hk.vo.message.MessageVO;
import com.hk.vo.order.AliPayVO;
import com.hk.vo.order.OrderVO;
import com.hk.vo.plan.MemberPlanVO;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private UserService userService;
    @Autowired
    private MemberPlanService memberPlanService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private AlipayManager alipayManager;

    @Override
    public OrderVO getOrderById(Long id) {
        OrderInfoEntity orderInfo = this.getById(id);
        if (orderInfo != null) {
            UserVO userVO = userService.getInfo(orderInfo.getUserId());
            MemberPlanVO planVO = memberPlanService.getInfoById(orderInfo.getPlanId());
            OrderVO orderVO = OrderVO.converter(orderInfo);
            orderVO.setUserVO(userVO);
            orderVO.setPlanVO(planVO);
            return orderVO;
        }
        return null;
    }

    @Override
    public Page<OrderVO> selectOrderPage(OrderSearchParam searchParam) {
        LambdaQueryWrapper<OrderInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(searchParam.getUserId() != null, OrderInfoEntity::getUserId, searchParam.getUserId());
        queryWrapper.eq(searchParam.getStatus() != null, OrderInfoEntity::getStatus, searchParam.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(searchParam.getOrderNo()), OrderInfoEntity::getOrderNumber, searchParam.getOrderNo());
        queryWrapper.orderByDesc(OrderInfoEntity::getId);
        Page<OrderInfoEntity> page = this.page(new Page<>(searchParam.getPageNum(), searchParam.getPageSize()), queryWrapper);
        Page<OrderVO> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            pageResult.setRecords(page.getRecords().stream().map(OrderVO::converter).collect(Collectors.toList()));
        }
        return pageResult;
    }

    @Override
    public OrderVO payPlan(PayPlanVo payPlanVo) {

        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();Long userId = payPlanVo.getUserId();
        Long planId = payPlanVo.getPlanId();
        UserVO userVO = userService.getInfo(userId);
        if (userVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "用户不存在");
        }
        MemberPlanVO planVO = memberPlanService.getInfoById(planId);
        if (planVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "套餐不存在");
        }
        String orderNumber = UUID.randomUUID().toString();
        orderInfoEntity.setOrderNumber(orderNumber);
        orderInfoEntity.setOrderDate(new Date());
        orderInfoEntity.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
        orderInfoEntity.setUserId(userId);
        orderInfoEntity.setPlanId(planId);
        orderInfoEntity.setUserName(userVO.getUserName());
        orderInfoEntity.setPlanName(planVO.getName());
        orderInfoEntity.setAmount(planVO.getPrice());
        boolean save = this.save(orderInfoEntity);
        if (save) {
            MessageVO messageVO = new MessageVO(MessageTypeEnum.ORDER_CREATE.getCode(), orderInfoEntity.getId());
            messageProducer.sendDelayedMessage(messageVO, 15 * 60 * 1000);
            OrderVO orderVO = OrderVO.converter(orderInfoEntity);
            AliPayVO payVO = new AliPayVO();
            payVO.setOrderNo(orderNumber);
            payVO.setOrderId(orderVO.getId());
            payVO.setAmount(orderVO.getAmount());
            String subject = "购买" + planVO.getName();
            payVO.setSubject(subject);
            String qrCodeUrl = alipayManager.generateQrCodeImage(payVO);
            orderVO.setQrCodeUrl(qrCodeUrl);
            return orderVO;
        }
        return null;
    }

    @Override
    public boolean updateStatus(Long orderId, Integer status) {
        OrderVO order = getOrderById(orderId);
        if (order == null || order.getStatus().equals(OrderStatusEnum.FINISHED.getCode())) return false;
        LambdaUpdateWrapper<OrderInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderInfoEntity::getId, orderId);
        updateWrapper.set(OrderInfoEntity::getStatus, status);
        return this.update(updateWrapper);
    }

    @Override
    public Integer getOrderStatusById(Long id) {
        OrderInfoEntity orderInfo = this.getById(id);
        if (orderInfo==null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,"订单不存在");
        }
        return orderInfo.getStatus();
    }
}
