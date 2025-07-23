package com.hk.controller.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.entity.log.LoginLogEntity;
import com.hk.param.LogSearchParam;
import com.hk.service.log.LoginLogService;
import com.hk.vo.log.LoginLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * 删除登录日志表
     */
    @GetMapping("/delete")
    @Operation(summary = "删除登录日志表")
    @OperatorLog(value = "登录日志", desc = "删除登录日志表")
    @PreAuthorize("@ss.hasPermission('/login/log/delete')")
    public ResponseResult deleteLoginLog(@RequestParam Long id) {
        return loginLogService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }
    /**
     * 查询详情
     */
    @GetMapping("/get")
    @Operation(summary = "查询登录日志详情")
    @PreAuthorize("@ss.hasPermission('/login/log/get')")
//    @OperatorLog(value = "登录日志", desc = "查询详情")
    public ResponseResult<LoginLogVO> getLoginLogById(@RequestParam Long id) {
        return ResponseResult.success(loginLogService.getInfoById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询列表")
    @PreAuthorize("@ss.hasPermission('/login/log/page')")
//    @OperatorLog(value = "登录日志", desc = "查询列表")
    public ResponseResult<IPage<LoginLogVO>> selectLoginPage(@RequestBody LogSearchParam searchParam) {
        return ResponseResult.success(loginLogService.selectLoginPage(searchParam));
    }
}