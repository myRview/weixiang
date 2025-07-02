package com.hk.service.plan.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.plan.MemberPlanEntity;
import com.hk.enums.StatusEnum;
import com.hk.mapper.plan.MemberPlanMapper;
import com.hk.param.PlanSearchParam;
import com.hk.service.plan.MemberPlanService;
import com.hk.vo.plan.MemberPlanVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员套餐表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class MemberPlanServiceImpl extends ServiceImpl<MemberPlanMapper, MemberPlanEntity> implements MemberPlanService {
    @Override
    public Page<MemberPlanVO> selectPage(PlanSearchParam searchParam) {
        LambdaQueryWrapper<MemberPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(searchParam.getStatus() != null, MemberPlanEntity::getStatus, searchParam.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(searchParam.getName()), MemberPlanEntity::getName, searchParam.getName());
        Integer pageNum = searchParam.getPageNum();
        Integer pageSize = searchParam.getPageSize();
        Page<MemberPlanEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        Page<MemberPlanVO> pageResult = new Page<>(pageNum, pageSize);
        pageResult.setTotal(page.getTotal());
        List<MemberPlanEntity> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            pageResult.setRecords(records.stream().map(MemberPlanVO::convert).collect(Collectors.toList()));
        }
        return pageResult;
    }

    @Override
    public MemberPlanVO getInfoById(Long id) {
        MemberPlanEntity plan = this.getById(id);
        if (plan != null) {
            return MemberPlanVO.convert(plan);
        }
        return null;
    }

    @Override
    public List<MemberPlanVO> selectList(StatusEnum statusEnum) {
        LambdaQueryWrapper<MemberPlanEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberPlanEntity::getStatus, statusEnum.getCode());
        List<MemberPlanEntity> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.stream().map(MemberPlanVO::convert).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean savePlan(MemberPlanVO planVO) {
        MemberPlanEntity planEntity = new MemberPlanEntity();
        BeanUtil.copyProperties(planVO, planEntity);
        return this.save(planEntity);
    }

    @Override
    public boolean updatePlan(MemberPlanVO planVO) {
        MemberPlanEntity planEntity = new MemberPlanEntity();
        BeanUtil.copyProperties(planVO, planEntity);
        return this.updateById(planEntity);
    }
}
