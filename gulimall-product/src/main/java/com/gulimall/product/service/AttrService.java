package com.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.product.entity.AttrEntity;
import com.gulimall.product.vo.AttrGroupRelationVo;
import com.gulimall.product.vo.AttrRespVo;
import com.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:10:32
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVo getAttrRespVo(Long attrId);

    void updateAttrVO(AttrVo attr);

    List<AttrEntity> queryAttrRelation(Long attrgroupId);

    void relationDelete(AttrGroupRelationVo[] vos);
}

