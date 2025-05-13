package com.selfstudy.controller;

import com.alipay.easysdk.factory.Factory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.selfstudy.common.Result;
import com.selfstudy.config.AliPayConfig;
import com.selfstudy.pojo.AliPay;
import com.selfstudy.pojo.Payorder;
import com.selfstudy.pojo.User;
import com.selfstudy.service.PayorderService;
import com.selfstudy.service.UserService;
import com.selfstudy.util.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;


import java.util.HashMap;
import java.util.Map;
/**
 * @Author
 * @Date Created in  2023/5/5 15:23
 * @DESCRIPTION:
 * @Version V1.0
 */
@RestController
@RequestMapping("/alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;
    @Autowired
    PayorderService payorderService;
    @Autowired
    UserService userService;



    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @GetMapping("/pay") // 前端路径参数格式?subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public void payNotify( HttpServletRequest request, HttpServletResponse resp) throws Exception {

        Payorder payorder = new Payorder();

        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");
            // 支付宝验签

            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                payorder.setTraceNo((Integer.parseInt(params.get("out_trade_no"))));
                Boolean isSuccess = payorderService.save(payorder);
                System.out.println("订单号保存成功"+isSuccess);
            }
        }

    }
    @PostMapping("/qualify")  // 注意这里必须是POST接口
    public void qualify(@RequestBody Map<String, Object> requestBody,
                        HttpServletRequest request, HttpServletResponse resp) throws Exception {
        Integer tradeNo = (Integer) requestBody.get("tradeNo");
        Integer userId = (Integer) requestBody.get("userId");
        Payorder payorder = new Payorder();
        QueryWrapper<Payorder>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trace_no", tradeNo);
        payorder = payorderService.getOne(queryWrapper);
        if (payorder == null) {
            Result result=Result.error("订单不存在");
            WebUtil.writeJson(resp, result);
        }else{
            User user = new User();
            user=userService.getById(userId);
            user.setRole(2);
            userService.updateById(user);
            Result result=Result.ok(null);
            WebUtil.writeJson(resp, result);
        }
    }

}