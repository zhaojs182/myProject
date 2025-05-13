package com.selfstudy.vo;



import lombok.Data;

@Data
public class Pay {
    private String subject;           // 交易名称
    private String tradeStatus;       // 交易状态
    private String tradeNo;           // 支付宝交易凭证号
    private String outTradeNo;        // 商户订单号
    private String totalAmount;       // 交易金额
    private String buyerId;           // 买家在支付宝唯一id
    private String gmtPayment;        // 买家付款时间
    private String buyerPayAmount;    // 买家付款金额
}

