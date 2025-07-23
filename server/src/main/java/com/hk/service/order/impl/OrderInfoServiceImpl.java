package com.hk.service.order.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.cache.RedisService;
import com.hk.common.ErrorCode;
import com.hk.constants.BaseConstant;
import com.hk.context.UserContext;
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
import com.hk.vo.order.OrderStatisticsVO;
import com.hk.vo.order.OrderVO;
import com.hk.vo.plan.MemberPlanVO;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.plan.PlanStatisticsVO;
import com.hk.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private RedisService redisService;

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

        Long userId = payPlanVo.getUserId();
        Long planId = payPlanVo.getPlanId();
        UserVO userVO = userService.getInfo(userId);
        if (userVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "用户不存在");
        }
        MemberPlanVO planVO = memberPlanService.getInfoById(planId);
        if (planVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "套餐不存在");
        }
        OrderVO vo = (OrderVO) redisService.getHash(getHashKey(), String.valueOf(userId));
        if (vo != null) {
            return vo;
        }
        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
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
            OrderVO orderVO = OrderVO.converter(orderInfoEntity);
            AliPayVO payVO = new AliPayVO();
            payVO.setOrderNo(orderNumber);
            payVO.setOrderId(orderVO.getId());
            payVO.setAmount(orderVO.getAmount());
            String subject = "购买" + planVO.getName();
            payVO.setSubject(subject);
            String qrCodeUrl = alipayManager.generateQrCodeImage(payVO);
            orderVO.setQrCodeUrl(qrCodeUrl);
            redisService.putHash(getHashKey(), String.valueOf(userId), orderVO);
            redisService.expire(getHashKey(), 15 * 60);
            MessageVO messageVO = new MessageVO(MessageTypeEnum.ORDER_CREATE.getCode(), orderInfoEntity.getId());
            messageProducer.sendDelayedMessage(messageVO, 15 * 60 * 1000);
            return orderVO;
        }
        return null;
    }


    @Override
    public boolean updateStatus(Long orderId, Integer status) {
        OrderVO order = getOrderById(orderId);
        if (order == null || order.getStatus().equals(OrderStatusEnum.FINISHED.getCode())) return false;
        redisService.deleteHash(getHashKey(), String.valueOf(order.getUserId()));
        LambdaUpdateWrapper<OrderInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderInfoEntity::getId, orderId);
        updateWrapper.set(OrderInfoEntity::getStatus, status);
        return this.update(updateWrapper);
    }

    @Override
    public Integer getOrderStatusById(Long id) {
        OrderVO vo = (OrderVO) redisService.getHash(getHashKey(), String.valueOf(UserContext.getCurrentUserId()));
        if (vo != null) {
            return vo.getStatus();
        }
        OrderInfoEntity orderInfo = this.getById(id);
        if (orderInfo == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "订单不存在");
        }
        return orderInfo.getStatus();
    }

    @Override
    public List<OrderStatisticsVO> getCount(String date) {
        List<OrderStatisticsVO> statisticsVOList = new ArrayList<>(3);
        //已支付
        OrderStatisticsVO payVO = new OrderStatisticsVO(0, OrderStatusEnum.FINISHED.getCode(), BigDecimal.ZERO);
        //待支付
        OrderStatisticsVO waitPayVO = new OrderStatisticsVO(0, OrderStatusEnum.WAIT_PAY.getCode(), BigDecimal.ZERO);
        //已取消
        OrderStatisticsVO cancelVO = new OrderStatisticsVO(0, OrderStatusEnum.CANCEL.getCode(), BigDecimal.ZERO);
        statisticsVOList.add(payVO);
        statisticsVOList.add(waitPayVO);
        statisticsVOList.add(cancelVO);
        //查询订单时间是date的所有订单
        LambdaQueryWrapper<OrderInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(date)) queryWrapper.eq(OrderInfoEntity::getOrderDate, date);
        queryWrapper.select(OrderInfoEntity::getStatus, OrderInfoEntity::getAmount);
        List<OrderInfoEntity> orderList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(orderList)) {
            Map<Integer, List<OrderInfoEntity>> map = orderList.parallelStream().collect(Collectors.groupingBy(OrderInfoEntity::getStatus));
            for (OrderStatisticsVO statisticsVO : statisticsVOList) {
                List<OrderInfoEntity> list = map.get(statisticsVO.getStatus());
                if (CollectionUtil.isEmpty(list)) continue;
                statisticsVO.setTotal(list.size());
                statisticsVO.setTotalAmount(list.stream().map(OrderInfoEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
        }
        return statisticsVOList;
    }

    @Override
    public List<PlanStatisticsVO> planCount(String date) {
        List<PlanStatisticsVO> planStatisticsVOS = new ArrayList<>();
        LambdaQueryWrapper<OrderInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(date)) queryWrapper.eq(OrderInfoEntity::getOrderDate, date);
        queryWrapper.eq(OrderInfoEntity::getStatus, OrderStatusEnum.FINISHED.getCode());
        queryWrapper.select(OrderInfoEntity::getPlanName, OrderInfoEntity::getId);
        List<OrderInfoEntity> orderList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(orderList)) {
            Map<String, List<OrderInfoEntity>> map = orderList.parallelStream().collect(Collectors.groupingBy(OrderInfoEntity::getPlanName));
            for (Map.Entry<String, List<OrderInfoEntity>> entry : map.entrySet()) {
                PlanStatisticsVO planStatisticsVO = new PlanStatisticsVO(entry.getKey(), entry.getValue().size());
                planStatisticsVOS.add(planStatisticsVO);
            }
        }
        return planStatisticsVOS;
    }

    private String getHashKey() {
        return String.format("%s:order", BaseConstant.CACHE_PREFIX);
    }
}
