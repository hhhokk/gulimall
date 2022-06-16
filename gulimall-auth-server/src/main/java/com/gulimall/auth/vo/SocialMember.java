package com.gulimall.auth.vo;

import lombok.Data;

/**
 * @author zy
 * @create 2022-06-15-0:20
 */
@Data
public class SocialMember {
    /**
     * 社交账号ID
     */
    private String socialUid;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String header;
}
