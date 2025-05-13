package com.selfstudy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.pojo.Category;
import com.selfstudy.service.CategoryService;
import com.selfstudy.util.JwtUtils;
import com.selfstudy.util.ThreadLocalUtil;
import com.selfstudy.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getInfo")
    public void getInfo(@RequestHeader(value = "Authorization", required = false) String authorization,
                        HttpServletRequest req, HttpServletResponse resp) {
        String token = authorization;
        if(token == null || token.isEmpty()){
            Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
            WebUtil.writeJson(resp,result);
        }
//        String token = req.getParameter("token");
//        System.out.println("这是收到的token，authorization版 = " + token);
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("create_user", id);
        List<Category> categories = categoryService.list(queryWrapper);
//        System.out.println("categories = " + categories);
        Map data =new HashMap();
        data.put("categories",categories);
        Result result = Result.ok(data);
        // 将Result对象转换为json响应给客户端
        WebUtil.writeJson(resp,result);
    }
    @PostMapping("/addInfo")
    public void addInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Category category,
            HttpServletResponse resp) {

        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        // 设置创建人和时间
        category.setCreateUser(id);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        // 保存分类信息
        categoryService.save(category);
        // 返回成功响应
        Result result = Result.ok(null);
        WebUtil.writeJson(resp, result);
    }
    @PutMapping("/updateInfo")
    public void updateInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Category category,
            HttpServletResponse resp) {
        try {
            // 解析 JWT Token，获取用户信息
            String token = authorization;
            Claims claims = JwtUtils.parseJwt(token);
            Integer id = claims.get("id", Integer.class);

            // 设置更新人和更新时间

            category.setUpdateTime(new Date());

            // 执行更新操作
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", category.getId());
            queryWrapper.eq("create_user", id);
            boolean success = categoryService.update(category, queryWrapper);
            // 根据操作结果返回响应
            if (success) {
                Result result = Result.ok(null);
                WebUtil.writeJson(resp, result);
            } else {
                Result result = Result.error("更新失败，请检查输入的分类信息！");
                WebUtil.writeJson(resp, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Result result = Result.error("系统异常，请稍后再试！");
            WebUtil.writeJson(resp, result);
        }
    }
    //解析json数据方法2
//        try {
//            // 从请求体中读取 JSON 数据
//            StringBuilder json = new StringBuilder();
//            BufferedReader reader = req.getReader();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                json.append(line);
//            }
//
//            // 解析 JSON 数据
//            ObjectMapper objectMapper = new ObjectMapper();
    //方法2的法1，解析完直接给category对象赋值
//            Category category = objectMapper.readValue(json.toString(), Category.class);
    //方法2的法2，先new一个对象，再赋值
//        // 使用 Jackson 解析 JSON 数据
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(jsonString);
//
//        // 单独提取 JSON 中的字段
//        String categoryName = jsonNode.get("categoryName").asText();
//        String categoryAlias = jsonNode.get("categoryAlias").asText();
//
//        // 打印提取的字段
//        System.out.println("categoryName = " + categoryName);
//        System.out.println("categoryAlias = " + categoryAlias);
    @RequestMapping("/deleteInfo")
    public void deleteInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam("id") Integer id,
            HttpServletResponse resp) {
        try {
            // 解析Token，获取用户信息
            String token = authorization;
            if (token == null || token.isEmpty()) {
                Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
                WebUtil.writeJson(resp, result);
                return;
            }
            Claims claims = JwtUtils.parseJwt(token);
            Integer userId = claims.get("id", Integer.class);

            // 检查该用户是否有权限删除该分类
            Category category = categoryService.getById(id);
            if (category == null) {
                Result result = Result.error("分类不存在");
                WebUtil.writeJson(resp, result);
                return;
            }
            if (!category.getCreateUser().equals(userId)) {
                Result result = Result.error("没有权限删除该分类");
                WebUtil.writeJson(resp, result);
                return;
            }

            // 删除分类
            boolean isDeleted = categoryService.removeById(id);
            if (isDeleted) {
                Result result = Result.ok("删除成功");
                WebUtil.writeJson(resp, result);
            } else {
                Result result = Result.error("删除失败");
                WebUtil.writeJson(resp, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Result result = Result.error("系统异常，请稍后再试！");
            WebUtil.writeJson(resp, result);
        }
    }



}
