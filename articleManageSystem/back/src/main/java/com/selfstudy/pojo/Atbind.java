package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName atbind
 */
@TableName(value ="atbind")
@Data
public class Atbind implements Serializable {
    private Integer id;

    private Integer aid;

    private Integer tid;

    private static final long serialVersionUID = 1L;
}