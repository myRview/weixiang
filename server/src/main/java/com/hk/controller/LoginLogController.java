package com.hk.controller;

import com.hk.entity.LoginLogEntity;
import com.hk.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 登录日志表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/login/log")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 添加登录日志表
     */
    @PostMapping("/add")
    @Operation(summary = "添加登录日志表")
    public boolean add(@RequestBody LoginLogEntity loginLogEntity) {
        return loginLogService.save(loginLogEntity);
    }

    /**
     * 删除登录日志表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除登录日志表")
    public boolean delete(@PathVariable Long id) {
        return loginLogService.removeById(id);
    }

    /**
     * 修改登录日志表
     */
    @PostMapping("/update")
    @Operation(summary = "修改登录日志表")
    public boolean update(@RequestBody LoginLogEntity loginLogEntity) {
        return loginLogService.updateById(loginLogEntity);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询详情")
    public LoginLogEntity getById(@PathVariable Long id) {
        return loginLogService.getById(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询列表")
    public List<LoginLogEntity> list() {
        return loginLogService.list();
    }
}