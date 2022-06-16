package com.gulimall.auth.feign;

import com.gulimall.auth.vo.SocialMember;
import com.gulimall.auth.vo.UserLoginVo;
import com.gulimall.auth.vo.UserRegisterVo;
import com.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zy
 * @create 2022-06-12-22:49
 */
@FeignClient("gulimall-member")
public interface UserMemberService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegisterVo registerVo);


    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R oauth2login(@RequestBody SocialMember memberEntity);
}
