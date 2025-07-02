package com.hk.service.plan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.plan.MemberPlanEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.enums.StatusEnum;
import com.hk.param.PlanSearchParam;
import com.hk.vo.plan.MemberPlanVO;

import java.util.List;

/**
* <p>
    * 会员套餐表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface MemberPlanService extends IService<MemberPlanEntity> {

    Page<MemberPlanVO> selectPage(PlanSearchParam searchParam);

    MemberPlanVO getInfoById(Long id);

    List<MemberPlanVO> selectList(StatusEnum statusEnum);

    boolean savePlan(MemberPlanVO planVO);

    boolean updatePlan(MemberPlanVO planVO);
}