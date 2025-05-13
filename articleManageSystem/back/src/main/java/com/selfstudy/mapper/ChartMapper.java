package com.selfstudy.mapper;

import com.selfstudy.dto.CategoryFrequencyDTO;
import com.selfstudy.pojo.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 27959
* @description 针对表【chart】的数据库操作Mapper
* @createDate 2024-12-19 16:48:49
* @Entity com.selfstudy.pojo.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    List<CategoryFrequencyDTO> getCategoryFrequency();

}




