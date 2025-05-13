package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category implements Serializable {
    private Integer id;

    private String categoryName;

    private String categoryAlias;

    private Integer createUser;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}