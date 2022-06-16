package com.gulimall.member.exception;

/**
 * @author zy
 * @create 2022-06-12-23:30
 */
public class UserPhoneExistException extends RuntimeException {
    public UserPhoneExistException() {
        super("手机号已经存在，请更换手机号");
    }
}
