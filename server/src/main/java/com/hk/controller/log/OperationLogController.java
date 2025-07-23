package com.hk.controller.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.entity.log.OperationLogEntity;
import com.hk.param.LogSearchParam;
import com.hk.service.log.OperationLogService;
import com.hk.vo.log.OperationLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Tag(name = "操作日志管理")
@RestController
@RequestMapping("/operation/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;


    /**
     * 删除操作日志表
     */
    @GetMapping("/delete")
    @Operation(summary = "删除操作日志")
    @OperatorLog(value = "操作日志管理", desc = "删除操作日志")
    @PreAuthorize("@ss.hasPermission('/operation/log/delete')")
    public ResponseResult deleteOperationLog(@RequestParam Long id) {
        return operationLogService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    @Operation(summary = "查询操作日志详情")
    @PreAuthorize("@ss.hasPermission('/operation/log/get')")
//    @OperatorLog(value = "操作日志管理", desc = "查询操作日志详情")
    public ResponseResult<OperationLogVO> getOperationLogById(@RequestParam Long id) {
        return ResponseResult.success(operationLogService.getOperationLogById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询操作日志列表")
    @PreAuthorize("@ss.hasPermission('/operation/log/page')")
//    @OperatorLog(value = "操作日志管理", desc = "查询操作日志列表")
    public ResponseResult<IPage<OperationLogVO>> selectOperaLogPage(@RequestBody LogSearchParam searchParam) {
        return ResponseResult.success(operationLogService.selectOperaLogPage(searchParam));
    }
}