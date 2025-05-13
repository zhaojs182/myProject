package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.Captcha;
import com.selfstudy.service.CaptchaService;
import com.selfstudy.mapper.CaptchaMapper;
import org.springframework.stereotype.Service;

/**
* @author 27959
* @description 针对表【captcha】的数据库操作Service实现
* @createDate 2024-12-20 09:00:19
*/
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha>
    implements CaptchaService{

}




