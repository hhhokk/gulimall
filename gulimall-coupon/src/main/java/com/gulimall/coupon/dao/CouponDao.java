package com.gulimall.coupon.dao;

import com.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:54:46
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
