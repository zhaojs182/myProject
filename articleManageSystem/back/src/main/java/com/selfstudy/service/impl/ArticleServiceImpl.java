package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.Article;
import com.selfstudy.service.ArticleService;
import com.selfstudy.mapper.ArticleMapper;
import com.selfstudy.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 27959
* @description 针对表【article】的数据库操作Service实现
* @createDate 2024-12-06 11:01:29
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtils redisUtils;

    private static final String ARTICLE_LIST_KEY = "article_list";

    /**
     * 获取文章列表，支持缓存优化
     */
    public List<Article> getArticleList() {
        // 先尝试从 Redis 中获取缓存
        List<Article> articleList = (List<Article>) redisUtils.get(ARTICLE_LIST_KEY);
        if (articleList != null) {
            // 如果 Redis 中有缓存，直接返回
            System.out.println("从 Redis 中获取文章列表");
            return articleList;
        }

        // 如果 Redis 中没有缓存，查询数据库
        articleList = articleMapper.selectList(null); // 根据实际情况进行查询
        if (articleList != null && !articleList.isEmpty()) {
            // 如果数据库有数据，将数据缓存到 Redis 中，设置过期时间（例如 1 小时）
            redisUtils.set(ARTICLE_LIST_KEY, articleList, 1, TimeUnit.HOURS);
            System.out.println("从数据库获取文章列表并缓存到 Redis");
        }

        return articleList;
    }

}




