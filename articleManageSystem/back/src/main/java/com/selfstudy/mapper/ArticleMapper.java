package com.selfstudy.mapper;

import com.selfstudy.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 27959
* @description 针对表【article】的数据库操作Mapper
* @createDate 2024-12-06 11:01:29
* @Entity com.selfstudy.pojo.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {
    Integer getNewsetId();
}




