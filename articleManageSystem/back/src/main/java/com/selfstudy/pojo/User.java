package com.selfstudy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String userPic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String phonenum;

    private Integer role;

    private Integer state;

    private static final long serialVersionUID = 1L;
}