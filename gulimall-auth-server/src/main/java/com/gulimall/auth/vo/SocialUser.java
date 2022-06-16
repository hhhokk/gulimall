package com.gulimall.auth.vo;

import lombok.Data;

/**
 * @author zy
 * @create 2022-06-14-22:03
 */
@Data
public class SocialUser {

        private String access_token;

        private String token_type;

        private Integer expires_in;

        private String refresh_token;

        private String scope;

        private Integer created_at;
}
