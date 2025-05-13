package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.Payorder;
import com.selfstudy.service.PayorderService;
import com.selfstudy.mapper.PayorderMapper;
import org.springframework.stereotype.Service;

/**
* @author 27959
* @description 针对表【payorder】的数据库操作Service实现
* @createDate 2024-12-30 11:26:29
*/
@Service
public class PayorderServiceImpl extends ServiceImpl<PayorderMapper, Payorder>
    implements PayorderService{

}




