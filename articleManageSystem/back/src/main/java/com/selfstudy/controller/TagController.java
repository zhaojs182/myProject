package com.selfstudy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.pojo.Atbind;
import com.selfstudy.pojo.PageBean;
import com.selfstudy.pojo.Tag;
import com.selfstudy.service.AtbindService;
import com.selfstudy.service.TagService;
import com.selfstudy.service.TagcategoryService;
import com.selfstudy.util.JwtUtils;
import com.selfstudy.util.RedisUtils;
import com.selfstudy.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.selfstudy.util.RedisUtils.ARTICLE_DETAIL_KEY;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagcategoryService tagcategoryService;
    @Autowired
    private RedisUtils redisUtils;;
    @Autowired
    private TagService tagService;
    @Autowired
    private AtbindService atbindService;
    @RequestMapping("/addTag")
    public void addTag(@RequestHeader(value = "Authorization", required = false) String authorization,
                       @RequestBody Map<String, Object> addTagModel,
                       HttpServletResponse resp){
        Map<String, Object> tag = (Map<String, Object>) addTagModel.get("tag");
        // 获取 aid 和 tid
        Integer aid = (Integer) tag.get("aid");
        Integer tid = (Integer) tag.get("tid");
        Atbind atbind=new Atbind();
        atbind.setAid(aid);
        atbind.setTid(tid);
        atbindService.save(atbind);
        Result result = Result.ok(null);
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
            boolean isDeleted = tagService.removeById(id);
            if (isDeleted) {
                Result result = Result.ok(null);
                WebUtil.writeJson(resp, result);
            } else {
                Result result = Result.build(null, ResultCodeEnum.LAZY_ERROR);
                WebUtil.writeJson(resp, result);
            }
    }
    @RequestMapping("/getArticleTag")
    public void getArticleTag(@RequestHeader(value = "Authorization", required = false) String authorization,
                              @RequestParam("articleId") Integer articleId,HttpServletResponse resp){
        List<Atbind>atbinds=atbindService.list(new QueryWrapper<Atbind>().eq("aid", articleId));
        List<Tag>tagList=new ArrayList<>();
        for(Atbind atbind:atbinds){
            Integer tagId=atbind.getTid();
            Tag tag=tagService.getById(tagId);
            tagList.add(tag);
        }
        Map data=new HashMap();
        data.put("tagList",tagList);
        Result result=Result.ok(data);
        WebUtil.writeJson(resp, result);
    }
    @RequestMapping("/getInfo")
    public void getInfo(@RequestHeader(value = "Authorization", required = false) String authorization, HttpServletResponse resp) {
        // 解析Token，获取用户信息
        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        List<Tag> tags = tagService.list(new QueryWrapper<Tag>().eq("userid", id));
//        System.out.println("拿到这个用户的全部标签："+tags);
        Map data = new HashMap();
        data.put("tags", tags);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);
    }
    @RequestMapping("/getInfoList")
    public void getInfoList(HttpServletRequest req, HttpServletResponse resp,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "tagcategoryId", required = false) Integer tagcategoryId,
                            @RequestParam(value = "userid", required = false) Integer userid
                            ) {
//        System.out.println("这是分类id555555555555555555555555555tagcategoryId:" + tagcategoryId);
        // 设置 Redis 缓存的键名，分页+筛选条件

        Tag tag = new Tag();
        tag.setCid(tagcategoryId);
        tag.setUserid(userid);
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>(tag);
        Page<Tag> page = new Page<>(pageNum, pageSize);
        tagService.page(page, queryWrapper);
        List<Tag> list = page.getRecords();
        PageBean<Tag> pageBean = new PageBean<>();
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(list);
//        System.out.println("从数据库获取文章列表（分页）并缓存到 Redis");
//        System.out.println("这是111111111111111111111111要发送的pagesBean:"+pageBean);
        Map data = new HashMap();
        data.put("pageBean", pageBean);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);
    }
    @PostMapping("/addInfo")
    public void addInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Tag tag,
            HttpServletResponse resp) {
        System.out.println("这是要添加的tag信息："+tag);
        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer id = claims.get("id", Integer.class);
        tag.setUserid(id);
        Boolean flag;
        if(tag.getId()==null){
        flag = tagService.save(tag);
        }else{
           flag = tagService.updateById(tag);
        }
        if(flag){
            Result result = Result.ok(null);
            WebUtil.writeJson(resp, result);
        }else{
            Result result = Result.build(null, ResultCodeEnum.SAVE_ERROR);
            WebUtil.writeJson(resp, result);
        }
        // 返回成功响应
        // 设置创建人和时间
//        category.setCreateUser(id);
//        category.setCreateTime(new Date());
//        category.setUpdateTime(new Date());
//        // 保存分类信息
//        categoryService.save(category);
    }
}
