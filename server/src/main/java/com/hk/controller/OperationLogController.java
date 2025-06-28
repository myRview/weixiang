package com.hk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ResponseResult;
import com.hk.entity.OperationLogEntity;
import com.hk.param.LogSearchParam;
import com.hk.service.OperationLogService;
import com.hk.vo.log.OperationLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 添加操作日志表
     */
    @PostMapping("/add")
    @Operation(summary = "添加操作日志")
    public ResponseResult addOperationLog(@RequestBody OperationLogEntity operationLogEntity) {
        return operationLogService.save(operationLogEntity) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除操作日志表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除操作日志")
    public ResponseResult deleteOperationLog(@PathVariable Long id) {
        return operationLogService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改操作日志表
     */
    @PostMapping("/update")
    @Operation(summary = "修改操作日志")
    public ResponseResult updateOperationLog(@RequestBody OperationLogEntity operationLogEntity) {
        return operationLogService.updateById(operationLogEntity) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询操作日志详情")
    public ResponseResult<OperationLogVO> getOperationLogById(@PathVariable Long id) {
        return ResponseResult.success(operationLogService.getOperationLogById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询操作日志列表")
    public ResponseResult<Page<OperationLogVO>> selectOperaLogPage(@RequestBody LogSearchParam searchParam) {
        return ResponseResult.success(operationLogService.selectOperaLogPage(searchParam));
    }
}