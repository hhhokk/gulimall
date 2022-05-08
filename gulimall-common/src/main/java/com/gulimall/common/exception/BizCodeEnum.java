package com.gulimall.common.exception;

/**
 * 错误码列表
 * 10：同用
 * 001：参数格式校验
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14物流
 *
 * @author zy
 * @create 2022-02-28-0:56
 */
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架错误");

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
