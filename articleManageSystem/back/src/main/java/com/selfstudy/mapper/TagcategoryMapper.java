package com.selfstudy.mapper;

import com.selfstudy.pojo.Tagcategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 27959
* @description 针对表【tagcategory】的数据库操作Mapper
* @createDate 2024-12-27 10:55:27
* @Entity com.selfstudy.pojo.Tagcategory
*/
public interface TagcategoryMapper extends BaseMapper<Tagcategory> {

    List<Tagcategory> selectTagcategory(String tagcategoryname);

}




