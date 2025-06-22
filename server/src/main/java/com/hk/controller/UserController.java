package com.hk.controller;

import com.hk.entity.UserEntity;
import com.hk.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户表
     */
    @PostMapping("/add")
    @Operation(summary ="添加用户")
    public boolean add(@RequestBody UserEntity userEntity) {
        return userService.save(userEntity);
    }

    /**
     * 删除用户表
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="删除用户")
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    /**
     * 修改用户表
     */
    @PostMapping("/update")
    @Operation(summary ="修改用户")
    public boolean update(@RequestBody UserEntity userEntity) {
        return userService.updateById(userEntity);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary ="查询用户详情")
    public UserEntity getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @Operation(summary ="查询用户列表")
    public List<UserEntity> list() {
        return userService.list();
    }
}