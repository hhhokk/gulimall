package com.gulimall.member.dao;

import com.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 22:05:06
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
