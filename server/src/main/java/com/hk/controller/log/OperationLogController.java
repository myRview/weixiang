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
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志")
    @OperatorLog(value = "操作日志管理", desc = "删除操作日志")
    public ResponseResult deleteOperationLog(@PathVariable Long id) {
        return operationLogService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询操作日志详情")
    @OperatorLog(value = "操作日志管理", desc = "查询操作日志详情")
    public ResponseResult<OperationLogVO> getOperationLogById(@PathVariable Long id) {
        return ResponseResult.success(operationLogService.getOperationLogById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询操作日志列表")
    @OperatorLog(value = "操作日志管理", desc = "查询操作日志列表")
    public ResponseResult<IPage<OperationLogVO>> selectOperaLogPage(@RequestBody LogSearchParam searchParam) {
        return ResponseResult.success(operationLogService.selectOperaLogPage(searchParam));
    }
}