package com.hk.service.plan.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hk.common.ErrorCode;
import com.hk.common.ResponseResult;
import com.hk.context.UserContext;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.entity.plan.UserPlanEntity;
import com.hk.enums.OrderStatusEnum;
import com.hk.enums.StatusEnum;
import com.hk.exception.BusinessException;
import com.hk.mapper.plan.UserPlanMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.service.order.OrderInfoService;
import com.hk.service.plan.MemberPlanService;
import com.hk.service.plan.UserPlanService;
import com.hk.service.user.UserService;
import com.hk.vo.order.OrderVO;
import com.hk.vo.plan.MemberPlanVO;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.plan.UserPlan;
import com.hk.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 用户套餐表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Service
public class UserPlanServiceImpl extends ServiceImpl<UserPlanMapper, UserPlanEntity> implements UserPlanService {

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private MemberPlanService memberPlanService;

    @Override
    public UserPlan getPayPlan(Long userId) {
        LambdaQueryWrapper<UserPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPlanEntity::getUserId, userId);
        queryWrapper.eq(UserPlanEntity::getStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.last(" limit 1");
        queryWrapper.select(UserPlanEntity::getId, UserPlanEntity::getUserId, UserPlanEntity::getPlanId,
                UserPlanEntity::getOrderId, UserPlanEntity::getStartDate, UserPlanEntity::getEndDate);
        UserPlanEntity userPlanEntity = this.getOne(queryWrapper);
        return UserPlan.convert(userPlanEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserPlan(OrderVO orderVO) {
        OrderVO order = orderInfoService.getOrderById(orderVO.getId());
        if (order == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "订单不存在");
        }
        MemberPlanVO planVO = memberPlanService.getInfoById(order.getPlanId());
        if (planVO == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "套餐不存在");
        }

        OrderInfoEntity orderInfo = new OrderInfoEntity();
        orderInfo.setId(order.getId());
        orderInfo.setStatus(OrderStatusEnum.FINISHED.getCode());
        orderInfoService.updateById(orderInfo);

        UserPlanEntity userPlanEntity = new UserPlanEntity();
        userPlanEntity.setUserId(order.getUserId());
        userPlanEntity.setPlanId(order.getPlanId());
        userPlanEntity.setOrderId(order.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(planVO.getValidityDays());
        userPlanEntity.setStartDate(startDate);
        userPlanEntity.setEndDate(endDate);
        userPlanEntity.setStatus(StatusEnum.NORMAL.getCode());
        return this.save(userPlanEntity);
    }
}
