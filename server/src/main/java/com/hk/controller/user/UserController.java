package com.hk.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ErrorCode;
import com.hk.common.ResponseResult;
import com.hk.context.UserContext;
import com.hk.exception.BusinessException;
import com.hk.param.UserSearchParam;
import com.hk.service.user.UserService;
import com.hk.vo.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

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
    @OperatorLog(value = "用户管理", desc = "添加用户")
    public ResponseResult addUser(@RequestBody UserAddVO addVO) {
        return userService.saveUser(addVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @OperatorLog(value = "用户管理", desc = "删除用户")
    public ResponseResult deleteUser(@PathVariable Long id) {
        return userService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改用户
     */
    @PostMapping("/update")
    @Operation(summary = "修改用户")
    @OperatorLog(value = "用户管理", desc = "修改用户")
    public ResponseResult updateUser(@RequestBody UserEditVO editVO) {
        return userService.updateUser(editVO) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 修改用户
     */
    @PostMapping("/edit")
    @Operation(summary = "编辑用户资料")
//    @OperatorLog(value = "用户管理", desc = "编辑用户资料")
    public ResponseResult editUser(@RequestBody EditUserExpandVO expandVO) {
        return userService.editUser(expandVO) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    @OperatorLog(value = "用户管理", desc = "查询用户详情")
    public ResponseResult<UserVO> getUserById(@PathVariable Long id) {
        return ResponseResult.success(userService.getInfo(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询用户列表")
    @OperatorLog(value = "用户管理", desc = "查询用户列表")
    public ResponseResult<Page<UserVO>> getUserList(@RequestBody UserSearchParam userSearchParam) {
        return ResponseResult.success(userService.getUserList(userSearchParam));
    }


    /**
     * 启用/禁用用户
     */
    @PostMapping("/status")
    @Operation(summary = "启用/禁用用户")
    @OperatorLog(value = "用户管理", desc = "查询用户列表")
    public ResponseResult transStatus(Long userId, Integer status) {
        return userService.transStatus(userId, status) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @Operation(summary = "修改密码")
    @OperatorLog(value = "用户管理", desc = "修改密码")
    public ResponseResult updatePassword(String password) {
        UserCacheVo userCacheVo = UserContext.getCurrentUser();
        if (userCacheVo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        Long userId = userCacheVo.getUserId();
        return userService.updatePassword(password, userId) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }


    /**
     * 重置密码
     */
    @PostMapping("/reset/pwd")
    @Operation(summary = "重置密码")
    @OperatorLog(value = "用户管理", desc = "重置密码")
    public ResponseResult resetPassword(Long userId) {
        return userService.resetPassword(userId) ? ResponseResult.success("操作成功") : ResponseResult.fail("操作失败");
    }

    /**
     * 获取当前用户
     */
    @PostMapping("/info")
    @Operation(summary = "获取当前用户")
    public ResponseResult<UserVO> getLoginUser() {
        UserCacheVo userCacheVo = UserContext.getCurrentUser();
        if (userCacheVo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        return ResponseResult.success(userCacheVo.getUser());
    }

    /**
     * 用户签到
     */
    @PostMapping("/sign")
    @Operation(summary = "用户签到")
    @OperatorLog(value = "用户签到", desc = "用户签到")
    public ResponseResult sign() {
        return userService.sign() ? ResponseResult.success("签到成功") : ResponseResult.fail("签到失败");
    }

    /**
     * 是否签到
     */
    @PostMapping("/isSigned")
    @Operation(summary = "是否签到")
    public ResponseResult isSigned() {
        return userService.isSigned() ? ResponseResult.success("已签到") : ResponseResult.fail("未签到");
    }


    /**
     * 获取本月签到天数
     *
     * @return
     */
    @PostMapping("/month/count")
    @Operation(summary = "获取本月签到天数")
    public ResponseResult getMonthSignCount() {
        return ResponseResult.success(userService.getMonthSignCount());
    }


    /**
     * 获取连续签到天数
     *
     * @return
     */
    @PostMapping("/continuous/count")
    @Operation(summary = "获取连续签到天数")
    public ResponseResult getContinuousSignCount() {
        return ResponseResult.success(userService.getContinuousSignCount());
    }

    /**
     * 获取签到记录
     *
     * @return
     */
    @GetMapping("/sign/record")
    @Operation(summary = "获取签到记录")
    public ResponseResult<Map<LocalDate, Boolean>> getSignRecord(@RequestParam(required = false) Integer year) {
        return ResponseResult.success(userService.getSignRecord(year));
    }
}