package com.gulimall.member.controller;

import com.gulimall.common.exception.BizCodeEnum;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.R;
import com.gulimall.member.entity.MemberEntity;
import com.gulimall.member.service.MemberService;
import com.gulimall.member.vo.LoginUserVo;
import com.gulimall.member.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 会员
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 22:05:06
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 社交用户登录
     */
    @PostMapping("/oauth2/login")
    public R oauth2login(@RequestBody MemberEntity memberEntity) {
        MemberEntity entity = memberService.login(memberEntity);
        if(StringUtils.isEmpty(entity)){
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getMessage());
        }
        return R.ok().put("data",entity);

    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginUserVo loginUserVo) {
        MemberEntity memberEntity = memberService.login(loginUserVo);
        if(StringUtils.isEmpty(memberEntity)){
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getMessage());
        }
            return R.ok().put("data",memberEntity);

    }

    /**
     * 用户注册
     */
    @PostMapping("/regist")
    public R regist(@RequestBody RegisterVo registerVo) {
        try {
            memberService.UserRegister(registerVo);
        } catch (Exception e) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMessage());
        }
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
