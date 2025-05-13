package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName chart
 */
@TableName(value ="chart")
@Data
public class Chart implements Serializable {
    private Integer id;

    private Integer articleId;

    private Integer categoryId;

    private Integer userId;

    private static final long serialVersionUID = 1L;
}