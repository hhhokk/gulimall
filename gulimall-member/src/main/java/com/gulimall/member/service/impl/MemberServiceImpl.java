package com.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;
import com.gulimall.member.dao.MemberDao;
import com.gulimall.member.entity.MemberEntity;
import com.gulimall.member.entity.MemberLevelEntity;
import com.gulimall.member.exception.UserPhoneExistException;
import com.gulimall.member.service.MemberLevelService;
import com.gulimall.member.service.MemberService;
import com.gulimall.member.vo.LoginUserVo;
import com.gulimall.member.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {


    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void UserRegister(RegisterVo registerVo) {
        checkPhoneUnique(registerVo.getPhone());//检查手机号是否唯一
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(registerVo.getUserName());
        //密码md5盐值加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(registerVo.getPassword());
        memberEntity.setPassword(encodePassword);
        memberEntity.setMobile(registerVo.getPhone());
        memberEntity.setLevelId(setDefaultMemberLevel());//设置默认会员等级
        baseMapper.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws UserPhoneExistException {
        Long mobile = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile != 0) {
            throw new UserPhoneExistException();
        }
    }

    @Override
    public MemberEntity login(LoginUserVo loginUserVo) {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginUserVo.getLoginacct()).or().eq("mobile", loginUserVo.getLoginacct()));
        if (!StringUtils.isEmpty(memberEntity)) {
            String password = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(loginUserVo.getPassword(), password);
            if (matches) {
                return memberEntity;
            }
        }
        return null;
    }

    @Override
    public MemberEntity login(MemberEntity memberEntity) {
        String socialUid = memberEntity.getSocialUid();
        MemberEntity socialUidEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", socialUid));
        if (StringUtils.isEmpty(socialUidEntity)) { //注册
            MemberEntity entity = new MemberEntity();
            entity.setLevelId(setDefaultMemberLevel());
            BeanUtils.copyProperties(memberEntity, entity);
            baseMapper.insert(entity);
        }
        return socialUidEntity;
    }

    public long setDefaultMemberLevel() {
        MemberLevelEntity levelEntity = memberLevelService.getOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));
        return levelEntity.getId();
    }

}