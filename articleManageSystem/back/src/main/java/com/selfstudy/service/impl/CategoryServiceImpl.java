package com.selfstudy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.selfstudy.pojo.Category;
import com.selfstudy.service.CategoryService;
import com.selfstudy.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 27959
* @description 针对表【category】的数据库操作Service实现
* @createDate 2024-12-06 11:01:29
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




