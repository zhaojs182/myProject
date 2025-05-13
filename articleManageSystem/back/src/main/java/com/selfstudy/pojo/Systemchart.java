package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName systemchart
 */
@TableName(value ="systemchart")
@Data
public class Systemchart implements Serializable {
    private Integer id;

    private Integer usernumber;

    private Integer articlenumber;

    private Integer order;

    private static final long serialVersionUID = 1L;
}