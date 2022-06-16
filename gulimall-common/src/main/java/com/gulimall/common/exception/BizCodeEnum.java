package com.gulimall.common.exception;

/**
 * 错误码列表
 * 10：通用
 *   001 ：参数格式错误
 *   002：发送短信频率过高
 * 001：参数格式校验
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14物流
 * 15用户模块
 *
 * @author zy
 * @create 2022-02-28-0:56
 */
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002, "获取验证码频率过高"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架错误"),
    PHONE_EXIST_EXCEPTION(15001, "该手机号已经注册"),
    LOGINACCT_PASSWORD_INVALID_EXCEPTION(15002, "用户名和密码错误");


    private Integer code;
    private String message;

    BizCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
