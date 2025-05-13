package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName tagcategory
 */
@TableName(value ="tagcategory")
@Data
public class Tagcategory implements Serializable {
    private Integer id;

    private String tagcategory;

    private Integer userid;

    private static final long serialVersionUID = 1L;
}