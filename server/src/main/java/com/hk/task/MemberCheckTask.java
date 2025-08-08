package com.hk.task;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.hk.entity.plan.UserPlanEntity;
import com.hk.enums.StatusEnum;
import com.hk.service.plan.UserPlanService;
import com.hk.vo.plan.UserPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangkun
 * @date 2025/8/8 16:12
 */
@Slf4j
@Component
public class MemberCheckTask {

    @Autowired
    private UserPlanService userPlanService;


    /**
     * Cron表达式：每天的00:00执行
     * Cron语法：秒 分 时 日 月 周 年（年可选）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void check() {
        List<UserPlan> userPlans = userPlanService.selectByStatus(StatusEnum.NORMAL.getCode());
        if (CollectionUtil.isEmpty(userPlans)) {
            return;
        }
        ArrayList<UserPlanEntity> updateData = new ArrayList<>();
        for (UserPlan userPlan : userPlans) {
            LocalDate endDate = userPlan.getEndDate();
            //如果会员到期日小于等于今天，则将会员状态改为已过期
            if (endDate.isBefore(LocalDate.now())) {
                UserPlanEntity userPlanEntity = new UserPlanEntity();
                userPlanEntity.setId(userPlan.getId());
                userPlanEntity.setStatus(StatusEnum.DISABLE.getCode());
                updateData.add(userPlanEntity);
            }
        }
        if (CollectionUtil.isEmpty(updateData)) return;
        log.info("会员到期检查，更新数据：{}", JSONObject.toJSONString(updateData));
        userPlanService.updateBatchById(updateData);
    }
}
