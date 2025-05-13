package com.schoolwork.epsys.acl.controller;

import com.schoolwork.epsys.acl.service.RolesService;
import com.schoolwork.epsys.acl.service.UsertoroleService;
import com.schoolwork.epsys.model.acl.Usertorole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    UsertoroleService usertoroleService;

    @RequestMapping("/addUsertoRole3")
    public Boolean addUsertoRole3(@RequestParam("userId") Integer userId,@RequestParam("roleId") Integer roleId) {
        Usertorole usertorole = new Usertorole();
        usertorole.setRoleId(roleId);
        usertorole.setUserId(userId);  // 使用传入的 userId
        Boolean flag = usertoroleService.save(usertorole);
        return flag;
    }


}
