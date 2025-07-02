package com.hk.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ResponseResult;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.param.OrderSearchParam;
import com.hk.service.order.OrderInfoService;
import com.hk.vo.order.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "添加订单")
    public ResponseResult addOrder(@RequestBody OrderInfoEntity orderInfoEntity) {
        return orderInfoService.save(orderInfoEntity) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    /**
     * 删除订单表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    public ResponseResult deleteOrder(@PathVariable Long id) {
        return orderInfoService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改订单表
     */
    @PostMapping("/update")
    @Operation(summary = "修改订单")
    public ResponseResult updateOrder(@RequestBody OrderInfoEntity orderInfoEntity) {
        return orderInfoService.updateById(orderInfoEntity) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询订单详情")
    public ResponseResult<OrderVO> getOrderById(@PathVariable Long id) {
        return ResponseResult.success(orderInfoService.getOrderById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询订单列表")
    public ResponseResult<Page<OrderVO>> selectOrderPage(@RequestBody OrderSearchParam searchParam) {
        return ResponseResult.success(orderInfoService.selectOrderPage(searchParam));
    }
}