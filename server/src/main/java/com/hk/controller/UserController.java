package com.hk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ResponseResult;
import com.hk.entity.UserEntity;
import com.hk.param.UserSearchParam;
import com.hk.service.UserService;
import com.hk.vo.user.UserAddVO;
import com.hk.vo.user.UserEditVO;
import com.hk.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 添加用户
     */
    @PostMapping("/add")
    @Operation(summary = "添加用户")
    public ResponseResult addUser(@RequestBody UserAddVO addVO) {
        return userService.saveUser(addVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ResponseResult deleteUser(@PathVariable Long id) {
        return userService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改用户
     */
    @PostMapping("/update")
    @Operation(summary = "修改用户")
    public ResponseResult updateUser(@RequestBody UserEditVO editVO) {
        return userService.updateUser(editVO) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    public ResponseResult<UserVO> getUserById(@PathVariable Long id) {
        return ResponseResult.success(userService.getInfo(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询用户列表")
    public ResponseResult<Page<UserVO>> getUserList(@RequestBody UserSearchParam userSearchParam) {
        return ResponseResult.success(userService.getUserList(userSearchParam));
    }


    /**
     * 启用/禁用用户
     */
    @PostMapping("/status")
    @Operation(summary = "启用/禁用用户")
    public ResponseResult transStatus(Long userId, Integer status) {
        return userService.transStatus(userId, status) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @Operation(summary = "修改密码")
    public ResponseResult updatePassword(String password) {
        return userService.updatePassword(password) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }


    /**
     * 重置密码
     */
    @PostMapping("/reset/pwd")
    @Operation(summary = "重置密码")
    public ResponseResult resetPassword(Long userId) {
        return userService.resetPassword(userId) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }
}