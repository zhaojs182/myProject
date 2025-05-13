package com.selfstudy.util;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import static com.aliyun.teautil.Common.toJSONString;

public class SmsUtil {

    // 创建短信发送客户端
    private static com.aliyun.dysmsapi20170525.Client createClient()throws Exception {
        return SendMessage.createClient();
    }
    // 发送短信验证码
    public static void sendSms(String phone, String code) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient();

        // 设置发送短信的请求参数
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("是sevetar1号签名") // 替换为实际签名
                .setTemplateCode("SMS_476900178") // 替换为实际模板编号
                .setPhoneNumbers(phone) // 用户手机号
                .setTemplateParam("{\"code\":\"" + code + "\"}"); // 动态验证码

        // 设置运行选项
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

        try {
            // 发送请求
            client.sendSmsWithOptions(sendSmsRequest, runtime);
            System.out.println("验证码已发送至: " + phone);
        } catch (TeaException error) {
            // 处理异常
            System.out.println("错误信息: " + error.getMessage());
            System.out.println("诊断地址: " + error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            // 捕获其它异常
            TeaException error = new TeaException(_error.getMessage(), _error);
            System.out.println("错误信息: " + error.getMessage());
            System.out.println("诊断地址: " + error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}

