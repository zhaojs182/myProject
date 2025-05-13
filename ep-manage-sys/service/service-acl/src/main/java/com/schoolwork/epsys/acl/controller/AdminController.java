package com.schoolwork.epsys.acl.controller;


import com.schoolwork.epsys.acl.mapper.PermissionsMapper;
import com.schoolwork.epsys.acl.service.PermissionsService;
import com.schoolwork.epsys.acl.service.UserService;
import com.schoolwork.epsys.model.acl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//这里写用户管理的接口
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    PermissionsMapper permissionsMapper;

    @Autowired
    UserService userService;
    @RequestMapping("/roleRequest")
    public Boolean roleRequest(@RequestParam Integer userId,          // 从 ?userId=xxx 获取
                            @RequestParam Integer permissionId) {
        return permissionsMapper.hasPermission(userId, permissionId);
    }
    @RequestMapping("/getUsernameById")
    public String getUsernameById(@RequestParam Integer userId) {
        User user =userService.getById(userId);
        return user.getUsername();
    }
}
