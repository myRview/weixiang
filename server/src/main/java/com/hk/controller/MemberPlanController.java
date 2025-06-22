package com.hk.controller;

import com.hk.entity.MemberPlanEntity;
import com.hk.service.MemberPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 会员套餐表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Tag(name = "会员套餐管理")
@RestController
@RequestMapping("/member/plan")
public class MemberPlanController {

    @Autowired
    private MemberPlanService memberPlanService;

    /**
     * 添加会员套餐表
     */
    @PostMapping("/add")
    @Operation(summary ="添加会员套餐")
    public boolean add(@RequestBody MemberPlanEntity memberPlanEntity) {
        return memberPlanService.save(memberPlanEntity);
    }

    /**
     * 删除会员套餐表
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="删除会员套餐")
    public boolean delete(@PathVariable Long id) {
        return memberPlanService.removeById(id);
    }

    /**
     * 修改会员套餐表
     */
    @PostMapping("/update")
    @Operation(summary ="修改会员套餐")
    public boolean update(@RequestBody MemberPlanEntity memberPlanEntity) {
        return memberPlanService.updateById(memberPlanEntity);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary ="查询会员套餐详情")
    public MemberPlanEntity getById(@PathVariable Long id) {
        return memberPlanService.getById(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @Operation(summary ="查询会员套餐列表")
    public List<MemberPlanEntity> list() {
        return memberPlanService.list();
    }
}