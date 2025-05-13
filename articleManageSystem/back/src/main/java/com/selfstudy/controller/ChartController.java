package com.selfstudy.controller;

import com.selfstudy.common.Result;
import com.selfstudy.dto.CategoryFrequencyDTO;
import com.selfstudy.dto.CategoryUsageDTO;
import com.selfstudy.mapper.ChartMapper;
import com.selfstudy.pojo.Category;
import com.selfstudy.service.CategoryService;
import com.selfstudy.service.ChartService;
import com.selfstudy.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@Validated
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ChartService chartService;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/categoryFrequency")
    @ResponseBody
    public void getChartData(HttpServletRequest req, HttpServletResponse resp) {
        // 获取所有种类信息
        List<Category> categories = categoryService.list();

        // 获取每个类别的文章使用次数
        List<CategoryFrequencyDTO> categoryFrequencyDTOS = chartMapper.getCategoryFrequency();

        // 创建一个新的集合来存储 categoryName 和 articleCount
        List<CategoryUsageDTO> categoryUsageDTOS = new ArrayList<>();

        // 使用两个集合进行映射
        for (CategoryFrequencyDTO categoryFrequencyDTO : categoryFrequencyDTOS) {
            // 根据 categoryId 获取 categoryName
            String categoryName = categories.stream()
                    .filter(category -> category.getId().equals(categoryFrequencyDTO.getCategoryId()))
                    .map(Category::getCategoryName)
                    .findFirst()
                    .orElse("未知类别"); // 如果找不到对应的类别名称，返回"未知类别"

            // 将 categoryName 和 articleCount 封装到 CategoryUsageDTO 中
            categoryUsageDTOS.add(new CategoryUsageDTO(categoryName, categoryFrequencyDTO.getArticleCount()));
        }

        Map data = new HashMap();
        data.put("categoryUsageDTOS",categoryUsageDTOS);
        Result result = Result.ok(data);
        WebUtil.writeJson(resp,result);
    }
}
