package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.User;
import com.selfstudy.service.UserService;
import com.selfstudy.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 27959
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-12-27 10:40:44
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




