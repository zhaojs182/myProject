package com.selfstudy.util;


import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.io.File;

public class CodeUtil {

    public static void code(){
        QrConfig config = new QrConfig();
        config.setErrorCorrection(ErrorCorrectionLevel.L);
        config.setBackColor(Color.GRAY);
        config.setWidth(300);
        config.setHeight(300);
        QrCodeUtil.generate("http://47.108.93.135", config, new File("E://IDEAwrokspacee//bigevent//src//main//java//com//selfstudy//1123.jpg"));
//        QrCodeUtil.generate("http://47.108.93.135", 300, 300, new File("E://IDEAwrokspacee//bigevent//src//main//java//com//selfstudy//1123.jpg"));

    }

}
