package com.hk.manager;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hk.common.ErrorCode;
import com.hk.config.MyAlipayConfig;
import com.hk.config.RabbitMQConfig;
import com.hk.enums.MessageTypeEnum;
import com.hk.exception.BusinessException;
import com.hk.mq.MessageProducer;
import com.hk.mq.MessageVO;
import com.hk.vo.order.AliPayVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
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


    /**
     * 生成支付二维码(测试)
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
                String orderId = params.get("out_trade_no");
//                String alipayTradeNo = params.get("trade_no");
//                BigDecimal amount = new BigDecimal(params.get("total_amount"));
                MessageVO messageVO = new MessageVO(MessageTypeEnum.ORDER_COMPLETE.getCode(), Long.valueOf(orderId));
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
}