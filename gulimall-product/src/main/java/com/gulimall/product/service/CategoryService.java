package com.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.vo.Catelog2LevelVo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:10:32
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeByIdList(List<Long> asList);

    Long[] findCatelogPath(Long attrGroupId);

    List<CategoryEntity> getLevelOneCategory();

    Map<String, List<Catelog2LevelVo>> getCatelogJson();
}

