package com.gulimall.ware.dao;

import com.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 22:27:23
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    List<WareSkuEntity> selectListBySkuIds(@Param("skuIds") List<Long> skuIds);
}
