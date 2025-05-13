package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName payorder
 */
@TableName(value ="payorder")
@Data
public class Payorder implements Serializable {
    private Integer id;

    private Integer traceNo;

    private static final long serialVersionUID = 1L;
}