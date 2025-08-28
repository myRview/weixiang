package com.hk.service.plan.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.cache.RedisService;
import com.hk.common.ErrorCode;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.entity.plan.UserPlanEntity;
import com.hk.enums.OrderStatusEnum;
import com.hk.enums.PushTypeEnum;
import com.hk.enums.StatusEnum;
import com.hk.exception.BusinessException;
import com.hk.mapper.plan.UserPlanMapper;
import com.hk.scoket.PushMessageBaseVO;
import com.hk.scoket.WebSocketPushService;
import com.hk.service.order.OrderInfoService;
import com.hk.service.plan.MemberPlanService;
import com.hk.service.plan.UserPlanService;
import com.hk.vo.message.OrderMessageVO;
import com.hk.vo.plan.MemberPlanVO;
import com.hk.vo.plan.UserPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户套餐表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Slf4j
@Service
public class UserPlanServiceImpl extends ServiceImpl<UserPlanMapper, UserPlanEntity> implements UserPlanService {

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private MemberPlanService memberPlanService;
    @Autowired
    private WebSocketPushService webSocketPushService;
    @Autowired
    private RedisService redisService;

    @Override
    public UserPlan getPayPlan(Long userId) {
        LambdaQueryWrapper<UserPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPlanEntity::getUserId, userId);
        queryWrapper.eq(UserPlanEntity::getStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.last(" limit 1");
        queryWrapper.select(UserPlanEntity::getId, UserPlanEntity::getUserId, UserPlanEntity::getPlanId,
                UserPlanEntity::getOrderId, UserPlanEntity::getStartDate, UserPlanEntity::getEndDate, UserPlanEntity::getStatus);
        UserPlanEntity userPlanEntity = this.getOne(queryWrapper);
        return UserPlan.convert(userPlanEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserPlan(Long orderId) {
        OrderInfoEntity order = orderInfoService.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "订单不存在");
        }
        //检查订单是否完成
        if (!order.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode())) {
            return true;
        }
        MemberPlanVO planVO = memberPlanService.getInfoById(order.getPlanId());
        if (planVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "套餐不存在");
        }
        Long userId = order.getUserId();
        OrderStatusEnum orderStatus = OrderStatusEnum.CANCEL;
        String lockKey = "order:create:lock:" + userId;
        String requestId = UUID.randomUUID().toString();
        boolean locked = false;
        //检查当前用户是否购买套餐
        try {
            locked = redisService.tryLock(lockKey, requestId, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE, "系统繁忙，请稍后再试");
            }
            UserPlanEntity saveEntry = buildUserPlan(userId, order, planVO);
            boolean save = this.save(saveEntry);
            if (save) {
                boolean updateStatus = orderInfoService.updateStatus(orderId, OrderStatusEnum.FINISHED.getCode());
                if (!updateStatus) {
                    log.error("订单状态更新失败，order:{}", order);
                    throw new BusinessException(ErrorCode.ERROR_SYSTEM, "订单状态更新失败");
                }
                //发送消息通知前端订单已完成
                OrderMessageVO orderMessageVO = new OrderMessageVO();
                orderMessageVO.setOrderId(orderId);
                orderMessageVO.setOrderStatus(orderStatus.getCode());
                PushMessageBaseVO<OrderMessageVO> messageBaseVO = new PushMessageBaseVO(PushTypeEnum.ORDER.getCode(), orderMessageVO);
                webSocketPushService.sendMessageToUser(userId.toString(), messageBaseVO);
            }
            return save;
        } finally {
            if (locked) {
                redisService.unlock(lockKey, requestId);
            }
        }
    }


    @Override
    public List<UserPlan> selectByStatus(int code) {
        LambdaQueryWrapper<UserPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPlanEntity::getStatus, code);
        queryWrapper.select(UserPlanEntity::getId, UserPlanEntity::getEndDate, UserPlanEntity::getStatus);
        List<UserPlanEntity> userPlanEntityList = this.list(queryWrapper);
        return userPlanEntityList.stream().map(UserPlan::convert).collect(Collectors.toList());
    }


    private UserPlanEntity buildUserPlan(Long userId, OrderInfoEntity order, MemberPlanVO planVO) {
        UserPlanEntity saveEntry = new UserPlanEntity();
        saveEntry.setUserId(userId);
        saveEntry.setPlanId(order.getPlanId());
        saveEntry.setOrderId(order.getId());
        saveEntry.setStatus(StatusEnum.NORMAL.getCode());

        LambdaQueryWrapper<UserPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPlanEntity::getUserId, userId);
        queryWrapper.eq(UserPlanEntity::getStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.select(UserPlanEntity::getId, UserPlanEntity::getStartDate, UserPlanEntity::getEndDate);
        queryWrapper.last(" limit 1");
        UserPlanEntity exitPlan = this.getOne(queryWrapper);
        if (exitPlan != null) {
            //当前用户已购买套餐
            LocalDate planEndDate = exitPlan.getEndDate();
            LocalDate endDate = planEndDate.plusDays(planVO.getValidityDays());
            saveEntry.setStartDate(exitPlan.getStartDate());
            saveEntry.setEndDate(endDate);

            exitPlan.setStartDate(null);
            exitPlan.setEndDate(null);
            exitPlan.setStatus(StatusEnum.DISABLE.getCode());
            this.updateById(exitPlan);
        } else {
            //当前用户未购买套餐
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(planVO.getValidityDays());
            saveEntry.setStartDate(startDate);
            saveEntry.setEndDate(endDate);
        }
        return saveEntry;
    }
}
