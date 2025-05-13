package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    private Integer id;

    private String title;

    private String content;

    private String coverImg;

    private String state;

    private Integer categoryId;

    private Integer createUser;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}