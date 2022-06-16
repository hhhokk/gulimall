package com.gulimall.auth.feign;

import com.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zy
 * @create 2022-06-08-21:52
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartService {

    @GetMapping("/sms/sendcode")
    R sendSCode(@RequestParam("phone") String phone, @RequestParam("code")String code);
}
