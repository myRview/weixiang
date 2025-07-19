package com.hk.service.plan;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.common.ResponseResult;
import com.hk.entity.plan.UserPlanEntity;
import com.hk.vo.order.OrderVO;
import com.hk.vo.plan.PayPlanVo;
import com.hk.vo.plan.UserPlan;

/**
* <p>
    * 用户套餐表 服务类
    * </p>
*
* @author hk
* @since 2025-06-30
*/
public interface UserPlanService extends IService<UserPlanEntity> {

    UserPlan getPayPlan(Long userId);


    boolean saveUserPlan(Long orderId);
}