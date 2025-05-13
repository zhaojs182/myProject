package com.schoolwork.epsys.common;

public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    USERNAEM_ERROR(501,"usernameError"),
    PASSWORD_ERROR(503,"passwordError"),
    NOTLOGIN(504,"notlogin"),
    USERNAME_USED(505,"usernameUsed"),
    SERVICE_ERROR(0,"serviceError"),
    LOGIN_ERROR(401,"loginError"),
    UPDATA_ERROR(506,"updataError"),
    REGISTER_ERROR(507,"registerExist"),
    SAVE_ERROR(508,"saveError"),
    LAZY_ERROR(509,"lazyError");


    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code ,String message){
        this.code= code;
        this.message = message;

    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
