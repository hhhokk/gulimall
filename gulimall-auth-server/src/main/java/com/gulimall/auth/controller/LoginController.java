package com.gulimall.auth.controller;

import com.gulimall.auth.feign.ThirdPartService;
import com.gulimall.auth.feign.UserMemberService;
import com.gulimall.auth.utils.Auth2Utils;
import com.gulimall.auth.vo.UserLoginVo;
import com.gulimall.auth.vo.UserRegisterVo;
import com.gulimall.common.constant.AuthServerConstant;
import com.gulimall.common.exception.BizCodeEnum;
import com.gulimall.common.to.MemberEntity;
import com.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zy
 * @create 2022-06-08-21:48
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartService thirdPartService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserMemberService userMemberService;

    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone) {
        //TODO 接口防刷

        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            Long aLong = Long.valueOf(redisCode.split("_")[1]);
            //60秒防刷
            if (System.currentTimeMillis() - aLong < 60 * 1000) {
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMessage());
            }
        }
        String code = String.valueOf(Math.random() * 1000000).substring(0, 4);
        String saveRedisCode = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, saveRedisCode, 5, TimeUnit.MINUTES);
        thirdPartService.sendSCode(phone, code);

        return R.ok();
    }

    @PostMapping("/regist")
    public String regist(@Valid UserRegisterVo vo, BindingResult result,
                         RedirectAttributes attributes) {
        if (result.hasErrors()) {

            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            model.addAttribute("errors",errors);
            attributes.addFlashAttribute("errors", errors);
            return "redirect:"+ Auth2Utils.REG_PAGE;
        }
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isEmpty(redisCode)) { //验证码为空
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:"+ Auth2Utils.REG_PAGE;
        }
        String code = redisCode.split("_")[0];
        if (code.equals(vo.getCode())) {
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
            R r = userMemberService.regist(vo);
            if (r.getCode() == 0) {
                return "redirect:"+ Auth2Utils.LOGIN_PAGE;
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("msg", r.getMessage());
                attributes.addFlashAttribute("errors", errors);
                return "redirect:"+ Auth2Utils.REG_PAGE;
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:"+ Auth2Utils.REG_PAGE;
        }

    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session){
        R login = userMemberService.login(vo);
        if(login.getCode() == 0){
            MemberEntity data = (MemberEntity) login.get("data");
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:"+Auth2Utils.INDEX_PAGE;
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("msg", "用户名和密码错误");
        attributes.addFlashAttribute("errors", errors);
        return "redirect:http://auth.gulimall.com/login.html";
    }
}
