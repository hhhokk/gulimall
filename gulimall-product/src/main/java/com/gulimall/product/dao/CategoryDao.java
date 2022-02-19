package com.gulimall.product.dao;

import com.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:10:32
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
