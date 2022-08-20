package com.gulimall.cart.vo;

import lombok.Data;

/**
 * @author zy
 * @create 2022-07-24-17:36
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;

    private boolean tempUser = false; //判断是否为临时用户
}
