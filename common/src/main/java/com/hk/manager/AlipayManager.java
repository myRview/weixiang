package com.hk.manager;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hk.common.ErrorCode;
import com.hk.config.MyAlipayConfig;
import com.hk.config.RabbitMQConfig;
import com.hk.entity.order.OrderInfoEntity;
import com.hk.enums.MessageTypeEnum;
import com.hk.enums.OrderStatusEnum;
import com.hk.exception.BusinessException;
import com.hk.mapper.order.OrderInfoMapper;
import com.hk.mq.MessageProducer;
import com.hk.mq.MessageVO;
import com.hk.vo.order.AliPayVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AlipayManager {

    private final AlipayConfig alipayConfig;
    private final MyAlipayConfig myAlipayConfig;

    @Autowired
    public AlipayManager(AlipayConfig alipayConfig, MyAlipayConfig myAlipayConfig) {
        this.alipayConfig = alipayConfig;
        this.myAlipayConfig = myAlipayConfig;
    }

    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private OrderInfoMapper orderInfoMapper;


    /**
     * 生成支付二维码(测试)
     *
     * @param response
     */
    public void generateQrCodeImage(HttpServletResponse response) {
        try {
            // 1. 初始化支付宝客户端
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            // 2. 创建预支付请求
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(myAlipayConfig.getNotifyUrl());
            // 3. 设置业务参数
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", "1545484123154545156");
            bizContent.put("total_amount", new BigDecimal("88.88"));
            bizContent.put("subject", "测试生成订单");
            request.setBizContent(bizContent.toJSONString());

            // 4. 执行API调用
            AlipayTradePrecreateResponse alipayResponse = alipayClient.execute(request);
            if (!alipayResponse.isSuccess() || alipayResponse.getQrCode() == null) {
                throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "支付宝预创建失败: " + alipayResponse.getSubMsg());
            }
            // 5. 获取支付宝返回的支付链接
            String qrCodeUrl = alipayResponse.getQrCode();
            // 6. 生成二维码图片字节流
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 300, 300);
            // 7. 将二维码写入响应流
            response.setContentType("image/png");
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
        } catch (AlipayApiException | WriterException | IOException e) {
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "二维码生成失败");
        }
    }

    /**
     * 支付回调
     *
     * @param request
     * @return
     */
    public String completePay(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        try {
            // 1. 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            if (!signVerified) {
                return "failure";
            }
            // 2. 验证交易状态
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                String orderIdStr = params.get("out_trade_no");
                if (StringUtils.isBlank(orderIdStr)) {
                    return "failure";
                }
                Long orderId;
                try {
                    orderId = Long.valueOf(orderIdStr);
                } catch (NumberFormatException e) {
                    log.error("订单ID格式错误: {}", orderIdStr);
                    return "failure";
                }
//                String alipayTradeNo = params.get("trade_no");
//                BigDecimal amount = new BigDecimal(params.get("total_amount"));
                //查询订单状态，仅待支付订单才发送消息
                OrderInfoEntity order = orderInfoMapper.selectOne(new LambdaQueryWrapper<OrderInfoEntity>()
                        .eq(OrderInfoEntity::getId, orderId)
                        .select(OrderInfoEntity::getStatus)
                );
                if (order == null || !order.getStatus().equals(OrderStatusEnum.WAIT_PAY.getCode())) {
                    log.info("订单已处理，无需重复发送消息，orderId: {}", orderId);
                    return "success";
                }

                MessageVO messageVO = new MessageVO(MessageTypeEnum.ORDER_COMPLETE.getCode(), orderId);
                messageProducer.send(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, messageVO);
                return "success";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "failure";
    }

    /**
     * 生成支付二维码
     *
     * @param payVO
     * @return
     */
    public String generateQrCodeImage(AliPayVO payVO) {
        try {
            String orderNo = payVO.getOrderNo();
            Long orderId = payVO.getOrderId();
            BigDecimal amount = payVO.getAmount();
            String subject = payVO.getSubject();

            // 1. 初始化支付宝客户端
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            // 2. 创建预支付请求
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(myAlipayConfig.getNotifyUrl());

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            // 设置商户订单号
            model.setOutTradeNo(String.valueOf(orderId));
            // 设置订单总金额
            model.setTotalAmount(amount.toString());
            // 设置订单标题
            model.setSubject(subject);
            // 设置PC扫码支付的方式
            model.setQrPayMode("1");
            // 设置商户自定义二维码宽度
            model.setQrcodeWidth(300L);
            request.setBizModel(model);
            // 设置订单包含的商品列表信息
//            List<GoodsDetail> goodsDetail = new ArrayList<>();
//            GoodsDetail goodsDetail0 = new GoodsDetail();
//            goodsDetail0.setGoodsName("ipad");
//            goodsDetail0.setAlipayGoodsId("20010001");
//            goodsDetail0.setQuantity(1L);
//            goodsDetail0.setPrice("2000");
//            goodsDetail0.setGoodsId("apple-01");
//            goodsDetail0.setGoodsCategory("34543238");
//            goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
//            goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
//            goodsDetail.add(goodsDetail0);
//            model.setGoodsDetail(goodsDetail);
            // 3. 设置业务参数
//            JSONObject bizContent = new JSONObject();
//            bizContent.put("out_trade_no", orderNo);
//            bizContent.put("total_amount", amount);
//            bizContent.put("subject", subject);
//            bizContent.put("qr_pay_mode", 1);
//            request.setBizContent(bizContent.toJSONString());
            // 4. 执行API调用
            AlipayTradePrecreateResponse alipayResponse = alipayClient.execute(request);
            if (!alipayResponse.isSuccess() || alipayResponse.getQrCode() == null) {
                throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "支付宝预创建失败: " + alipayResponse.getSubMsg());
            }
            // 5. 获取支付宝返回的支付链接
            String qrCodeUrl = alipayResponse.getQrCode();
            return qrCodeUrl;
        } catch (AlipayApiException e) {
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "二维码生成失败");
        }
    }

    public Boolean closeOrder(String orderId) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            // 构造请求参数以调用接口
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();

            //TODO： 注意：交易流水号和商户订单号二选一
//        // 设置该交易在支付宝系统中的交易流水号
//        model.setTradeNo("2013112611001004680073956707");
            // 设置订单支付时传入的商户订单号
            model.setOutTradeNo(orderId);
            request.setBizModel(model);
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            log.info("关闭订单响应：{}", response.getBody());
            if (response.isSuccess()) {
                return true;
            } else {
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.error("关闭订单失败，错误码：{}，错误信息：{}，诊断链接：{}", response.getCode(), response.getMsg(), response.getSubMsg(), diagnosisUrl);
                return false;
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败", e);
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "关闭订单失败");
        }
    }

    public Boolean refund(String orderId, BigDecimal refundAmount) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            // 设置商户订单号
            model.setOutTradeNo(orderId);
            // 或者设置支付宝交易号
//            model.setTradeNo("2014112611001004680073956707");
            // 设置退款金额
            model.setRefundAmount(refundAmount.toString());
            // 设置退款原因说明
            model.setRefundReason("正常退款");
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
//            log.info("退款响应：{}", response.getBody());
            if (response.isSuccess()) {
                return true;
            } else {
                return false;
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
        } catch (AlipayApiException e) {
            log.error("退款失败:{}", e);
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "退款失败");
        }
    }

    public JSONObject queryOrder(String orderId) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            // 设置商户订单号
            model.setOutTradeNo(orderId);
            // 或者设置支付宝交易号
//            model.setTradeNo("2014112611001004680073956707");
            request.setBizModel(model);
            AlipayTradeQueryResponse response = alipayClient.execute(request);
//            log.info("获取订单详情响应：{}", response.getBody());
            if (response.isSuccess()) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                JSONObject queryResponse = jsonObject.getJSONObject("alipay_trade_query_response");
                return queryResponse;
            } else {
                return null;
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
        } catch (AlipayApiException e) {
            log.error("获取订单信息失败：{}", e);
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "退款失败");
        }
    }

    public JSONObject queryRefund(String orderId) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            // 设置商户订单号
            model.setOutTradeNo(orderId);
            // 或者设置支付宝交易号
//            model.setTradeNo("2014112611001004680073956707");
            // 设置退款请求号
            model.setOutRequestNo(orderId);
            request.setBizModel(model);
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
//            log.info("获取订单详情响应：{}", response.getBody());
            if (response.isSuccess()) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                JSONObject queryResponse = jsonObject.getJSONObject("alipay_trade_fastpay_refund_query_response");
                return queryResponse;
            } else {
                return null;
                // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
                // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                // System.out.println(diagnosisUrl);
            }
        } catch (AlipayApiException e) {
            log.error("获取退款订单信息失败：{}", e);
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "退款失败");
        }
    }

}