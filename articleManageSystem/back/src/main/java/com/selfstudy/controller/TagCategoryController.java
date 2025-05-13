package com.selfstudy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.mapper.TagcategoryMapper;
import com.selfstudy.pojo.Category;
import com.selfstudy.pojo.Tagcategory;
import com.selfstudy.service.TagcategoryService;
import com.selfstudy.util.JwtUtils;
import com.selfstudy.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/tagCategory")
public class TagCategoryController {
    @Autowired
    private TagcategoryService tagcategoryService;
    @Autowired
    private TagcategoryMapper tagcategoryMapper;
    @GetMapping("/getInfo")
    public void getInfo(@RequestHeader(value = "Authorization", required = false) String authorization,
                        @RequestParam(value = "tagsearchtest", required = false) String tagsearchtest,
                        HttpServletRequest req, HttpServletResponse resp) {
        String token = authorization;
        if(token == null || token.isEmpty()){
            Result result = Result.build(null, ResultCodeEnum.LAZY_ERROR);
            WebUtil.writeJson(resp,result);
        }
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        QueryWrapper<Tagcategory>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid",id);
        if(tagsearchtest!=null&&!tagsearchtest.isEmpty()){
            List<Tagcategory> list =tagcategoryMapper.selectTagcategory(tagsearchtest);
            Map data =new HashMap();
            data.put("list",list);
            Result result = Result.ok(data);
            // 将Result对象转换为json响应给客户端
            WebUtil.writeJson(resp,result);
            return;
        }
        List<Tagcategory> list = tagcategoryService.list(queryWrapper);
//        System.out.println("这是tagcategory的list："+list);
        Map data =new HashMap();
        data.put("list",list);
        Result result = Result.ok(data);
        // 将Result对象转换为json响应给客户端
        WebUtil.writeJson(resp,result);
    }
    @PutMapping("/updateInfo")
    public void updateInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Tagcategory tagcategory,
            HttpServletResponse resp) {
        try {
            // 解析 JWT Token，获取用户信息
            String token = authorization;
            Claims claims = JwtUtils.parseJwt(token);
            Integer uesrid = claims.get("id", Integer.class);
            // 执行更新操作
            QueryWrapper<Tagcategory> wrapper = new QueryWrapper<>();
            wrapper.eq("id", tagcategory.getId());
            wrapper.eq("userid",uesrid);
            boolean success = tagcategoryService.update(tagcategory, wrapper);
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
            Tagcategory tagcategory = tagcategoryService.getById(id);
//            Category category = categoryService.getById(id);
            if (tagcategory == null) {
                Result result = Result.error("tag不存在");
                WebUtil.writeJson(resp, result);
                return;
            }
            if (!tagcategory.getUserid().equals(userId)) {
                Result result = Result.error("没有权限删除该分类");
                WebUtil.writeJson(resp, result);
                return;
            }

            // 删除分类
            boolean isDeleted = tagcategoryService.removeById(id);
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

    @PostMapping("/addInfo")
    public void addInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Tagcategory tagcategory,
            HttpServletResponse resp) {

        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        tagcategory.setUserid(id);
//        System.out.println("这是传回来44444444444444444的tagcategory："+tagcategory);
        Boolean result=tagcategoryService.save(tagcategory);
        if(result){
            Result result1 = Result.ok(null);
            WebUtil.writeJson(resp, result1);
        }else{
            Result result2 = Result.error("添加失败");
            WebUtil.writeJson(resp, result2);
        }
    }

}
