package com.hk.controller;

import com.hk.entity.OperationLogEntity;
import com.hk.service.OperationLogService;
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
    @Operation(summary ="添加操作日志")
    public boolean add(@RequestBody OperationLogEntity operationLogEntity) {
        return operationLogService.save(operationLogEntity);
    }

    /**
     * 删除操作日志表
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="删除操作日志")
    public boolean delete(@PathVariable Long id) {
        return operationLogService.removeById(id);
    }

    /**
     * 修改操作日志表
     */
    @PostMapping("/update")
    @Operation(summary ="修改操作日志")
    public boolean update(@RequestBody OperationLogEntity operationLogEntity) {
        return operationLogService.updateById(operationLogEntity);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary ="查询操作日志详情")
    public OperationLogEntity getById(@PathVariable Long id) {
        return operationLogService.getById(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @Operation(summary ="查询操作日志列表")
    public List<OperationLogEntity> list() {
        return operationLogService.list();
    }
}