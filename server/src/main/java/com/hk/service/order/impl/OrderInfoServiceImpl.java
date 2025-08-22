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
import com.hk.mq.MessageVO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
        queryWrapper.like(StringUtils.isNotBlank(searchParam.getUserName()), OrderInfoEntity::getUserName, searchParam.getUserName());
        queryWrapper.like(StringUtils.isNotBlank(searchParam.getPlanName()), OrderInfoEntity::getPlanName, searchParam.getPlanName());
        if (searchParam.getStartDate() != null && searchParam.getEndDate() != null) {
            queryWrapper.between(OrderInfoEntity::getOrderDate, searchParam.getStartDate(), searchParam.getEndDate());
        }
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

    @Transactional(rollbackFor = Exception.class)
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
        String lockKey = "order:create:lock:" + userId;
        String requestId = UUID.randomUUID().toString();
        boolean locked = false;
        try {
            // 尝试获取锁，设置30秒超时
            locked = redisService.tryLock(lockKey, requestId, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "系统繁忙，请稍后再试");
            }
            // 检查用户是否有未支付订单（双重检查）
            OrderVO existingOrder = (OrderVO) redisService.getHash(getHashKey(), String.valueOf(userId));
            if (existingOrder != null) {
                return existingOrder;
            }
            String orderNumber = generateOrderNumber(userId);
            // 创建订单实体
            OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
            orderInfoEntity.setOrderNumber(orderNumber);
            orderInfoEntity.setOrderDate(new Date());
            orderInfoEntity.setStatus(OrderStatusEnum.WAIT_PAY.getCode());
            orderInfoEntity.setUserId(userId);
            orderInfoEntity.setPlanId(planId);
            orderInfoEntity.setUserName(userVO.getUserName());
            orderInfoEntity.setPlanName(planVO.getName());
            orderInfoEntity.setAmount(planVO.getPrice());
            boolean saveSuccess = this.save(orderInfoEntity);
            if (!saveSuccess) {
                throw new BusinessException(ErrorCode.ERROR_SYSTEM, "订单创建失败");
            }

            //  TODO: 可以先生成二维码，避免创建无效订单
            AliPayVO payVO = new AliPayVO();
            payVO.setOrderId(orderInfoEntity.getId());
            payVO.setOrderNo(orderNumber);
            payVO.setAmount(planVO.getPrice());
            payVO.setSubject("购买" + planVO.getName());
            String qrCodeUrl = alipayManager.generateQrCodeImage(payVO);

            OrderVO orderVO = OrderVO.converter(orderInfoEntity);
            orderVO.setQrCodeUrl(qrCodeUrl);
            orderVO.setValidTime(15 * 60 * 1000L);
            payVO.setOrderId(orderVO.getId());
            // 缓存订单信息
            redisService.putHash(getHashKey(), String.valueOf(userId), orderVO);
            redisService.expire(getHashKey(), 15 * 60);

            // 发送延迟消息（15分钟后检查订单状态）
            MessageVO messageVO = new MessageVO(MessageTypeEnum.ORDER_CREATE.getCode(), orderInfoEntity.getId());
            messageProducer.sendDelayedMessage(messageVO, 15 * 60 * 1000);
            return orderVO;
        } finally {
            // 释放锁
            if (locked) {
                redisService.unlock(lockKey, requestId);
            }
        }
    }


    @Override
    public boolean updateStatus(Long orderId, Integer status) {
        OrderVO order = getOrderById(orderId);
        if (order == null || order.getStatus().equals(OrderStatusEnum.FINISHED.getCode())) return false;

        //乐观锁更新（防止并发覆盖，确保仅当状态为当前状态时才更新）
        LambdaUpdateWrapper<OrderInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrderInfoEntity::getId, orderId);
        updateWrapper.eq(OrderInfoEntity::getStatus, order.getStatus());
        updateWrapper.set(OrderInfoEntity::getStatus, status);
        boolean update = this.update(updateWrapper);
        if (update) {
            redisService.deleteHash(getHashKey(), String.valueOf(order.getUserId()));
        }
        return update;
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
        queryWrapper.select(OrderInfoEntity::getPlanName, OrderInfoEntity::getId, OrderInfoEntity::getPlanId);
        List<OrderInfoEntity> orderList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(orderList)) {
            Map<Long, List<OrderInfoEntity>> map = orderList.parallelStream().collect(Collectors.groupingBy(OrderInfoEntity::getPlanId));
            Map<Long, String> collect = orderList.parallelStream().collect(Collectors.toMap(OrderInfoEntity::getPlanId, OrderInfoEntity::getPlanName, (k1, k2) -> k1));
            for (Map.Entry<Long, List<OrderInfoEntity>> entry : map.entrySet()) {
                String planName = collect.get(entry.getKey());
                PlanStatisticsVO planStatisticsVO = new PlanStatisticsVO(planName, entry.getValue().size());
                planStatisticsVOS.add(planStatisticsVO);
            }
        }
        return planStatisticsVOS;
    }

    private String getHashKey() {
        return String.format("%s:order", BaseConstant.CACHE_PREFIX);
    }


    /**
     * 生成订单号：时间戳+用户ID+随机数
     */
    private String generateOrderNumber(Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s%s%s", timeStr, userId, uuid);
    }
}
