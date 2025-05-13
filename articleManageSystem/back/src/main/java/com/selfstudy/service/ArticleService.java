package com.selfstudy.service;

import com.selfstudy.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 27959
* @description 针对表【article】的数据库操作Service
* @createDate 2024-12-06 11:01:29
*/
public interface ArticleService extends IService<Article> {
    List<Article> getArticleList();

}
