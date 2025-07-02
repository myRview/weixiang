package com.hk.controller.plan;

import com.hk.entity.plan.UserPlanEntity;
import com.hk.service.plan.UserPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户套餐表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Tag(name = "用户套餐表管理")
@RestController
@RequestMapping("/user/plan")
public class UserPlanController {

    @Autowired
    private UserPlanService userPlanService;

    @PostMapping("/add")
    @Operation(summary = "添加用户套餐表")
    public boolean add(@RequestBody UserPlanEntity userPlanEntity) {
        return userPlanService.save(userPlanEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户套餐表")
    public boolean delete(@PathVariable Long id) {
        return userPlanService.removeById(id);
    }

    @PostMapping("/update")
    @Operation(summary = "修改用户套餐表")
    public boolean update(@RequestBody UserPlanEntity userPlanEntity) {
        return userPlanService.updateById(userPlanEntity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户套餐表详情")
    public UserPlanEntity getById(@PathVariable Long id) {
        return userPlanService.getById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "查询用户套餐表列表")
    public List<UserPlanEntity> list() {
        return userPlanService.list();
    }
}