package com.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.gulimall.product.entity.BrandEntity;
import com.gulimall.product.entity.CategoryBrandRelationEntity;
import com.gulimall.common.utils.PageUtils;


import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-03-17 22:30:25
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BrandEntity> getBrandByCatId(Long catId);
}

