package com.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.member.entity.MemberEntity;
import com.gulimall.member.exception.UserPhoneExistException;
import com.gulimall.member.vo.LoginUserVo;
import com.gulimall.member.vo.RegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 22:05:06
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void UserRegister(RegisterVo registerVo);

    void checkPhoneUnique(String phone) throws UserPhoneExistException;

    MemberEntity login(LoginUserVo loginUserVo);

    MemberEntity login(MemberEntity memberEntity);
}

