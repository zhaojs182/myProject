package com.selfstudy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.pojo.Article;
import com.selfstudy.pojo.PageBean;
import com.selfstudy.pojo.User;
import com.selfstudy.service.UserService;
import com.selfstudy.util.JwtUtils;
import com.selfstudy.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @RequestMapping("/DisableOrOpenUsers")
    public void DisableOrOpenUsers(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Map<String, Object> params,
            HttpServletResponse resp) throws IOException {

        // 获取前端传递的用户ID列表
        List<Integer> userIds = (List<Integer>) params.get("userlist");
//        System.out.println("这是用户ID列777777777777777777777777表："+userIds);
        if (userIds != null && !userIds.isEmpty()) {
            // 遍历用户ID列表
            for (Integer userId : userIds) {
                // 查找用户
                User user = userService.getById(userId);

                if (user != null) {
                    // 获取当前用户的状态
                    Integer currentState = user.getState();

                    // 如果状态为0，修改为1；如果为1，修改为0
                    user.setState(currentState == 0 ? 1 : 0);

                    // 更新用户信息
                    userService.updateById(user);
                }
            }
        }
        Result result = Result.ok(null);
        WebUtil.writeJson(resp, result);
    }

    @RequestMapping("/resetpassword")
    public void resetpassword(@RequestBody Map<String, Object> params, HttpServletResponse resp){
        Integer id = (Integer) params.get("id");  // 从请求体是中获取 id
        if (id == null) {
          Result result = Result.build(null, ResultCodeEnum.LAZY_ERROR);
          WebUtil.writeJson(resp, result);
        }
        User user = userService.getById(id);
        user.setPassword("123456");
        userService.updateById(user);
//        System.out.println("重置密码成功");
        Result result = Result.ok(null);
        WebUtil.writeJson(resp, result);
    }
    @RequestMapping("/getUsersInfo")
    public void getUsersInfo(HttpServletRequest req, HttpServletResponse resp,
                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                             @RequestParam(value = "userId", required = false) Integer userId,
                             @RequestParam(value = "state", required = false) String state){
       Page<User> page = new Page<>(pageNum, pageSize);
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       userService.page(page, queryWrapper);
       List<User> list = page.getRecords();
       PageBean<User> pageBean = new PageBean<>();
       pageBean.setTotal(page.getTotal());
       pageBean.setItems(list);
       System.out.println("这是888888888888888pageBean："+pageBean);
       Map data = new HashMap();
       data.put("pageBean", pageBean);
       Result result = Result.ok(data);
       WebUtil.writeJson(resp, result);
    }
    @RequestMapping("/deleteInfo")
    public void deleteInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam("id") Integer id,
            HttpServletResponse resp) {
        // 解析Token，获取用户信息
        String token = authorization;
        if (token == null || token.isEmpty()) {
            Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
            WebUtil.writeJson(resp, result);
            return;
        }
        Claims claims = JwtUtils.parseJwt(token);
        // 删除标签
        boolean isDeleted = userService.removeById(id);
        if (isDeleted) {
            Result result = Result.ok(null);
            WebUtil.writeJson(resp, result);
        } else {
            Result result = Result.build(null, ResultCodeEnum.LAZY_ERROR);
            WebUtil.writeJson(resp, result);
        }
    }





}
