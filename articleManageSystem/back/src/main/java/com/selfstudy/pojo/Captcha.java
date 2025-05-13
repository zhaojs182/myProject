package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName captcha
 */
@TableName(value ="captcha")
@Data
public class Captcha implements Serializable {
    private Integer id;

    private String newcaptcha;

    private static final long serialVersionUID = 1L;
}