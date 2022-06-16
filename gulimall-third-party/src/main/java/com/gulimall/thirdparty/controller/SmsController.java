package com.gulimall.thirdparty.controller;

import com.gulimall.common.utils.R;
import com.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zy
 * @create 2022-06-08-21:38
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("/sendcode")
    public R sendSCode(@RequestParam("phone") String phone,@RequestParam("code")String code){
        smsComponent.send_SMS(phone,code);
        return R.ok();
    }
}
