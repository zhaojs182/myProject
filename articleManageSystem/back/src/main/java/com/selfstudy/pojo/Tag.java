package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    private Integer id;

    private Integer cid;

    private String tname;

    private Integer userid;

    private static final long serialVersionUID = 1L;
}