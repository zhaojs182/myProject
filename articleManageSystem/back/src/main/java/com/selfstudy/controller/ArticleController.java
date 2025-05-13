package com.selfstudy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.selfstudy.common.Result;
import com.selfstudy.common.ResultCodeEnum;
import com.selfstudy.mapper.ArticleMapper;
import com.selfstudy.pojo.Article;
import com.selfstudy.pojo.Category;
import com.selfstudy.pojo.Chart;
import com.selfstudy.pojo.PageBean;
import com.selfstudy.service.ArticleService;
import com.selfstudy.service.ChartService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static com.selfstudy.util.RedisUtils.ARTICLE_DETAIL_KEY;
import static com.selfstudy.util.RedisUtils.ARTICLE_LIST_KEY;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ChartService chartService;
    @Autowired
    private RedisUtils redisUtils;
    @RequestMapping("/getDetailInfo")
    public void getDetailInfo(Integer id,HttpServletRequest req, HttpServletResponse resp){
        Article article = articleService.getById(id);
        Map data = new HashMap();
        data.put("article",article);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp,result);
    }
    @RequestMapping("/addArticle")
    public void addArticle(@RequestHeader(value = "Authorization", required = false) String authorization,
                           @RequestBody @Validated Article article, HttpServletRequest req, HttpServletResponse res) {
        String token = authorization;
        Claims claims = JwtUtils.parseJwt(token);
        Integer userId = claims.get("id", Integer.class);
        String username = claims.get("username", String.class);
        article.setCreateUser(userId);
        if(article.getCreateTime() == null){
            article.setCreateTime(new Date());
            article.setUpdateTime(new Date());
            articleService.save(article);
            article=articleService.getOne(new QueryWrapper<Article>().eq("title",article.getTitle()));
            Chart chart = new Chart();
            chart.setArticleId(article.getId());
            chart.setUserId(userId);
            chart.setCategoryId(article.getCategoryId());
            chartService.save(chart);
        }else{
            article.setUpdateTime(new Date());
            articleService.updateById(article);
        }
        redisUtils.deleteArticleListCache(); // 删除分页缓存
        // 返回结果
        Result result = Result.ok(null);
        WebUtil.writeJson(res, result);
    }

    @RequestMapping("/deleteInfo")
    public void deleteInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam("id") Integer id,
            HttpServletResponse resp) {
        try {
            String token = authorization;
            if (token == null || token.isEmpty()) {
                Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
                WebUtil.writeJson(resp, result);
                return;
            }
            Claims claims = JwtUtils.parseJwt(token);
            Integer userId = claims.get("id", Integer.class);
            // 检查该用户是否有权限删除该文章
            Article article = articleService.getById(id);
            if (article == null) {
                Result result = Result.error("文章不存在");
                WebUtil.writeJson(resp, result);
                return;
            }
            if (!article.getCreateUser().equals(userId)) {
                Result result = Result.error("没有权限删除该文章");
                WebUtil.writeJson(resp, result);
                return;
            }
            // 删除文章
            boolean isDeleted = articleService.removeById(id);
            if (isDeleted) {
                redisUtils.deleteArticleListCache();

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

    @RequestMapping("/getInfoList")
    public void getInfoList(HttpServletRequest req, HttpServletResponse resp,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                            @RequestParam(value = "state", required = false) String state) {
        // 设置 Redis 缓存的键名，分页+筛选条件
        String cacheKey = "article_list:" + pageNum + ":" + pageSize + ":" + (categoryId != null ? categoryId : "null") + ":" + (state != null ? state : "null");
        // 先尝试从 Redis 中获取缓存
        PageBean<Article> cachedPageBean = (PageBean<Article>) redisUtils.get(cacheKey);
        if (cachedPageBean != null) {
            System.out.println("从 Redis 中获取文章列表（分页）");
            // 如果有缓存，直接返回缓存的数据
            Map data = new HashMap();
            data.put("pageBean", cachedPageBean);
            Result result = Result.ok(data);
            WebUtil.writeJson(resp, result);
            return;
        }// 如果 Redis 中没有缓存，则查询数据库
        Article article = new Article();
        article.setCategoryId(categoryId);
        article.setState(state);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);// 创建分页对象
        Page<Article> page = new Page<>(pageNum, pageSize);
        articleService.page(page, queryWrapper);
        List<Article> list = page.getRecords();// 封装分页数据

        PageBean<Article> pageBean = new PageBean<>();
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(list);// 将查询结果缓存到 Redis，并设置缓存过期时间为 1 小时（根据实际情况调整）
        redisUtils.set(cacheKey, pageBean, 1, TimeUnit.HOURS);
        System.out.println("从数据库获取文章列表并缓存到 Redis");
        // 返回数据
        Map data = new HashMap();
        data.put("pageBean", pageBean);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp, result);
    }
    //    @RequestMapping("/deleteInfo")
//    public void deleteInfo(
//            @RequestHeader(value = "Authorization", required = false) String authorization,
//            @RequestParam("id") Integer id,
//            HttpServletResponse resp) {
//        try {
//            // 解析Token，获取用户信息
//            String token = authorization;
//            if (token == null || token.isEmpty()) {
//                Result result = Result.build(null, ResultCodeEnum.USERNAEM_ERROR);
//                WebUtil.writeJson(resp, result);
//                return;
//            }
//            Claims claims = JwtUtils.parseJwt(token);
//            Integer userId = claims.get("id", Integer.class);
//
//            // 检查该用户是否有权限删除该分类
//            Article article = articleService.getById(id);
//            if (article == null) {
//                Result result = Result.error("分类不存在");
//                WebUtil.writeJson(resp, result);
//                return;
//            }
//            if (!article.getCreateUser().equals(userId)) {
//                Result result = Result.error("没有权限删除该分类");
//                WebUtil.writeJson(resp, result);
//                return;
//            }
//            // 删除分类
//            boolean isDeleted = articleService.removeById(id);
//            if (isDeleted) {
//                Result result = Result.ok("删除成功");
//                WebUtil.writeJson(resp, result);
//            } else {
//                Result result = Result.error("删除失败");
//                WebUtil.writeJson(resp, result);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Result result = Result.error("系统异常，请稍后再试！");
//            WebUtil.writeJson(resp, result);
//        }
//    }
//        @RequestMapping("/addArticle")
//    public void addArticle(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestBody @Validated Article article, HttpServletRequest req, HttpServletResponse res) {
//        String token = authorization;
//        Claims claims = JwtUtils.parseJwt(token);
//        Integer userId = claims.get("id", Integer.class);
//        String username = claims.get("username", String.class);
////        System.out.println("这是id = " + userId+"，这是username11111111111111111111111111 = "+username);
////        System.out.println("这是aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa收到的article"+article);
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", article.getId());
//        Article article2= articleService.getOne(queryWrapper);
//        Boolean flag =false;
//        if(article2!=null){
//            flag=true;
//        }
//        System.out.println("看看差没查到11111111111111111"+flag);
//        Article article1 =new Article();
//        article1.setCreateUser(userId);
//        article1.setTitle(article.getTitle());
//        article1.setCategoryId(article.getCategoryId());
//        article1.setCoverImg(article.getCoverImg());
//        article1.setContent(article.getContent());
//        article1.setState(article.getState());
//        if(article1.getCreateTime()==null){
//            article1.setCreateTime(new Date());
//        }
//        article1.setUpdateTime(new Date());
//        if(!flag){
//            articleService.save(article1);
//
//            Integer id=articleMapper.getNewsetId();
//            article1=articleService.getById(id);
//            Chart chart = new Chart();
//            chart.setArticleId(article1.getId());
//            chart.setCategoryId(article1.getCategoryId());
//            chart.setUserId(article1.getCreateUser());
//            chartService.save(chart);
//            Result result = Result.ok(null);
//            WebUtil.writeJson(res, result);
//            return;
//        }
//        articleService.update(article1,queryWrapper);
//        Result result = Result.ok(null);
//        WebUtil.writeJson(res, result);
//    }
//    @RequestMapping("/getInfoList")
//    public void getInfoList(HttpServletRequest req, HttpServletResponse resp,
//                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
//                            @RequestParam(value = "state", required = false) String state){
//        Article article = new Article();
//        article.setCategoryId(categoryId);
//        article.setState(state);
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);
//        List<Article> articleList = articleService.list(queryWrapper);
////        System.out.println("这是查询到的文章列表：aaaaaaaaaaaa"+articleList);
//
//        Page<Article> page = new Page<>(pageNum, pageSize);
//        articleService.page(page, queryWrapper);
//        List<Article> list = page.getRecords();
//        PageBean<Article> pageBean = new PageBean<>();
//        pageBean.setTotal(page.getTotal());
//        pageBean.setItems(list);
////        System.out.println("这是查询到的文章列表："+list+"，总数："+page.getTotal()+"\n");
//        System.out.println("这是bean"+pageBean);
//        Map data = new HashMap();
//        data.put("pageBean", pageBean);
//        Result result = Result.ok(data);
//        WebUtil.writeJson(resp, result);
//
//    }

}
