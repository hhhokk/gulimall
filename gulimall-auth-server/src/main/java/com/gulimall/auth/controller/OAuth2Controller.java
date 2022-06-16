package com.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gulimall.auth.utils.Auth2Utils;
import com.gulimall.auth.feign.UserMemberService;
import com.gulimall.auth.vo.MemberEntity;
import com.gulimall.auth.vo.SocialMember;
import com.gulimall.auth.vo.SocialUser;
import com.gulimall.common.utils.HttpUtils;
import com.gulimall.common.utils.R;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @create 2022-06-14-21:17
 */
@Controller
public class OAuth2Controller {

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * grant_type=authorization_code
     * code={code}
     * client_id={client_id}
     * redirect_uri={redirect_uri}
     * client_secret={client_secret}
     * <p>
     * https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
     *
     * @param code
     * @return
     */
    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> body = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        Map<String, String> doPostQuerys = new HashMap<>();
        Map<String, String> doGetQuerys = new HashMap<>();
        body.put("grant_type", Auth2Utils.GRANT_TYPE);
        body.put("code", code);
        body.put("client_id", Auth2Utils.CLIENT_ID);
        body.put("redirect_uri", Auth2Utils.REDIRECT_URI);
        body.put("client_secret", Auth2Utils.CLIENT_SECRET);

        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", headers, doPostQuerys, body);
        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
//            String refresh_token = socialUser.getRefresh_token();
            String access_token = socialUser.getAccess_token();
            Integer expires_in = socialUser.getExpires_in();
            doGetQuerys.put("access_token", access_token);

        }
        //https://gitee.com/api/v5/user?access_token=75b019f3b964cb75c00c658a66557b0e
        HttpResponse httpResponse = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", headers, doGetQuerys);
        SocialMember socialMember = new SocialMember();
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String string = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSON.parseObject(string);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String avatar = jsonObject.getString("avatar_url");
            socialMember.setSocialUid(id);
            socialMember.setEmail(email);
            socialMember.setNickname(name);
            socialMember.setHeader(avatar);

        }
        R oauth2login = userMemberService.oauth2login(socialMember);
        if (oauth2login.getCode() == 0) {
//            MemberEntity data = oauth2login.getData("data",new TypeReference<MemberEntity>(){});
            Map map = (HashMap)oauth2login.getData();
            MemberEntity data = JSON.parseObject(JSON.toJSONString(map), MemberEntity.class);
            session.setAttribute("loginUser",data);

            System.out.println(data.toString());
            return "redirect:" +Auth2Utils.INDEX_PAGE;
        } else {
            return "redirect:"+ Auth2Utils.LOGIN_PAGE;
        }
    }

}
