package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName uesrchart
 */
@TableName(value ="uesrchart")
@Data
public class Uesrchart implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer articleId;

    private Integer commentId;

    private static final long serialVersionUID = 1L;
}