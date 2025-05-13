package com.selfstudy.dto;

public class CategoryFrequencyDTO {
    private Integer categoryId;  // categoryId
    private Integer articleCount;    // 文章数量

    // 构造函数
    public CategoryFrequencyDTO(Integer categoryId, Integer articleCount) {
        this.categoryId = categoryId;
        this.articleCount = articleCount;
    }

    // Getter 和 Setter 方法
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    // 重写 toString 方法，返回有意义的字符串
    @Override
    public String toString() {
        return "CategoryFrequencyDTO{" +
                "categoryId=" + categoryId +
                ", articleCount=" + articleCount +
                '}';
    }
}
