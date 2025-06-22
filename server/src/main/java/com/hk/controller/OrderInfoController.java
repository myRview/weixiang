package com.hk.controller;

import com.hk.entity.OrderInfoEntity;
import com.hk.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping("/order/info")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 添加订单表
     */
    @PostMapping("/add")
    @Operation(summary ="添加订单")
    public boolean add(@RequestBody OrderInfoEntity orderInfoEntity) {
        return orderInfoService.save(orderInfoEntity);
    }

    /**
     * 删除订单表
     */
    @DeleteMapping("/{id}")
    @Operation(summary ="删除订单")
    public boolean delete(@PathVariable Long id) {
        return orderInfoService.removeById(id);
    }

    /**
     * 修改订单表
     */
    @PostMapping("/update")
    @Operation(summary ="修改订单")
    public boolean update(@RequestBody OrderInfoEntity orderInfoEntity) {
        return orderInfoService.updateById(orderInfoEntity);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary ="查询订单详情")
    public OrderInfoEntity getById(@PathVariable Long id) {
        return orderInfoService.getById(id);
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    @Operation(summary ="查询订单列表")
    public List<OrderInfoEntity> list() {
        return orderInfoService.list();
    }
}