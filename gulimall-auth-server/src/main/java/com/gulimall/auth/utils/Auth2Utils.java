package com.gulimall.auth.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zy
 * @create 2022-06-14-21:43
 */
@Component
@PropertySource("classpath:auth2config.properties")
public class Auth2Utils implements InitializingBean {
    @Value("${OAuth.config.grant_type}")
    private String grant_type;
    @Value("${OAuth.config.client_id}")
    private String client_id;
    @Value("${OAuth.config.redirect_uri}")
    private String redirect_uri;
    @Value("${OAuth.config.client_secret}")
    private String client_secret;
    @Value("${OAuth.config.index_page}")
    private String index_page;
    @Value("${OAuth.config.login_page}")
    private String login_page;
    @Value("${OAuth.config.reg_page}")
    private String reg_page;

    public static String INDEX_PAGE;
    public static String LOGIN_PAGE;
    public static String REG_PAGE;
    public static String GRANT_TYPE;
    public static String CLIENT_ID;
    public static String REDIRECT_URI;
    public static String CLIENT_SECRET;
    @Override
    public void afterPropertiesSet() throws Exception {
        GRANT_TYPE = grant_type;
        CLIENT_ID = client_id;
        REDIRECT_URI = redirect_uri;
        CLIENT_SECRET = client_secret;
        INDEX_PAGE = index_page;
        LOGIN_PAGE = login_page;
        REG_PAGE =reg_page;
    }
}
