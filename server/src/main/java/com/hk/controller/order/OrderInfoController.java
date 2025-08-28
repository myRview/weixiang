package com.hk.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.aop.log.annotation.OperatorLog;
import com.hk.common.ResponseResult;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.param.OrderSearchParam;
import com.hk.service.order.OrderInfoService;
import com.hk.vo.order.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
//    @PostMapping("/add")
//    @Operation(summary = "添加订单")
//    @OperatorLog(value = "订单管理", desc = "添加订单")
//    public ResponseResult addOrder(@RequestBody OrderInfoEntity orderInfoEntity) {
//        return orderInfoService.save(orderInfoEntity) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
//    }

    /**
     * 删除订单表
     */
    @GetMapping("/delete")
    @Operation(summary = "删除订单")
    @OperatorLog(value = "订单管理", desc = "删除订单")
    @PreAuthorize("@ss.hasPermission('/order/info/delete')")
    public ResponseResult deleteOrder(@RequestParam Long id) {
        return orderInfoService.removeById(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 修改订单表
     */
//    @PostMapping("/update")
//    @Operation(summary = "修改订单")
//    @OperatorLog(value = "订单管理", desc = "修改订单")
//    public ResponseResult updateOrder(@RequestBody OrderInfoEntity orderInfoEntity) {
//        return orderInfoService.updateById(orderInfoEntity) ? ResponseResult.success("修改成功") : ResponseResult.fail("修改失败");
//    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    @Operation(summary = "查询订单详情")
    @OperatorLog(value = "订单管理", desc = "查询订单详情")
    @PreAuthorize("@ss.hasPermission('/order/info/get')")
    public ResponseResult<OrderVO> getOrderById(@RequestParam Long id) {
        return ResponseResult.success(orderInfoService.getOrderById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询订单列表")
    @PreAuthorize("@ss.hasPermission('/order/info/page')")
//    @OperatorLog(value = "订单管理", desc = "查询订单列表")
    public ResponseResult<Page<OrderVO>> selectOrderPage(@RequestBody OrderSearchParam searchParam) {
        return ResponseResult.success(orderInfoService.selectOrderPage(searchParam));
    }


    /**
     * 查询订单状态
     */
    @GetMapping("/status")
    @Operation(summary = "查询订单状态")
    @PreAuthorize("@ss.hasPermission('/order/info/status')")
    public ResponseResult<Integer> getOrderStatusById(@RequestParam Long id) {
        return ResponseResult.success(orderInfoService.getOrderStatusById(id));
    }

    /**
     * 查询支付宝订单详情
     */
    @GetMapping("/detail")
    @Operation(summary = "查询支付宝订单详情")
    @PreAuthorize("@ss.hasPermission('/order/info/detail')")
    public ResponseResult<JSONObject> getOrderDetail(@RequestParam Long id) {
        return ResponseResult.success(orderInfoService.getOrderDetail(id));
    }

    /**
     * 退款
     *
     */
    @GetMapping("/refund")
    @Operation(summary = "退款")
    @PreAuthorize("@ss.hasPermission('/order/info/refund')")
    public ResponseResult<?> refund(@RequestParam Long id) {
        return ResponseResult.success(orderInfoService.refund(id));
    }

    /**
     * 查询支付宝退款订单详情
     * @param id
     * @return
     */
    @GetMapping("/refund/detail")
    @Operation(summary = "查询支付宝退款订单详情")
    @PreAuthorize("@ss.hasPermission('/order/info/refund/detail')")
    public ResponseResult<JSONObject> getRefundDetail(@RequestParam Long id) {
        return ResponseResult.success(orderInfoService.getRefundDetail(id));
    }


}