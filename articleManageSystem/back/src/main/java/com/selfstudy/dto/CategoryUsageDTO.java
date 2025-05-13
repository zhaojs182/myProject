package com.selfstudy.dto;

public class CategoryUsageDTO {
    private String categoryName;  // 种类的名字
    private Integer articleCount; // 种类的使用次数

    // 构造函数
    public CategoryUsageDTO(String categoryName, Integer articleCount) {
        this.categoryName = categoryName;
        this.articleCount = articleCount;
    }

    // Getter 和 Setter 方法
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    // 重写 toString 方法
    @Override
    public String toString() {
        return "CategoryUsageDTO{" +
                "categoryName='" + categoryName + '\'' +
                ", articleCount=" + articleCount +
                '}';
    }
}
