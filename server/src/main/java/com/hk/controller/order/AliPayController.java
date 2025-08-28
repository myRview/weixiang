package com.hk.controller.order;

import com.hk.common.ResponseResult;
import com.hk.manager.AlipayManager;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangkun
 * @date 2025/7/7 15:46
 */
@RestController
@RequestMapping("/aliPay")
public class AliPayController {

    @Autowired
    private AlipayManager alipayManager;

    @Operation(summary = "生成支付二维码(测试)")
    @GetMapping("/pay")
    public ResponseResult pay(HttpServletResponse servletResponse) {
        alipayManager.generateQrCodeImage(servletResponse);
        return ResponseResult.success();
    }

    @Operation(summary = "支付回调")
    @PostMapping("/callback")
    public String callback(HttpServletRequest request) {
        return alipayManager.completePay(request);
    }
}
