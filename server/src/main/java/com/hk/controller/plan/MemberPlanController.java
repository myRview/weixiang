package com.hk.controller.plan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ResponseResult;
import com.hk.enums.StatusEnum;
import com.hk.param.PlanSearchParam;
import com.hk.service.plan.MemberPlanService;
import com.hk.vo.plan.MemberPlanVO;
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
    public ResponseResult addPlan(@RequestBody MemberPlanVO planVO) {
        return memberPlanService.savePlan(planVO)? ResponseResult.success("添加成功"):ResponseResult.fail("添加失败");
    }

    /**
     * 删除会员套餐表
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="删除会员套餐")
    public ResponseResult deletePlan(@PathVariable Long id) {
        return memberPlanService.removeById(id)? ResponseResult.success("删除成功"):ResponseResult.fail("删除失败");
    }

    /**
     * 修改会员套餐表
     */
    @PostMapping("/update")
    @Operation(summary ="修改会员套餐")
    public ResponseResult updatePlan(@RequestBody MemberPlanVO planVO) {
        return memberPlanService.updatePlan(planVO)? ResponseResult.success("修改成功"):ResponseResult.fail("修改失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary ="查询会员套餐详情")
    public ResponseResult<MemberPlanVO> getPlanInfoById(@PathVariable Long id) {
        return ResponseResult.success(memberPlanService.getInfoById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary ="查询会员套餐列表")
    public ResponseResult<Page<MemberPlanVO>> selectPlanPage(@RequestBody PlanSearchParam searchParam) {
        return ResponseResult.success(memberPlanService.selectPage(searchParam));
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @Operation(summary ="查询会员套餐列表")
    public ResponseResult<List<MemberPlanVO>> selectPlanList() {
        return ResponseResult.success(memberPlanService.selectList(StatusEnum.NORMAL));
    }
}