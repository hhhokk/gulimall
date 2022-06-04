package com.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;
import com.gulimall.product.dao.SkuInfoDao;
import com.gulimall.product.entity.SkuInfoEntity;
import com.gulimall.product.entity.SpuImagesEntity;
import com.gulimall.product.entity.SpuInfoDescEntity;
import com.gulimall.product.service.*;
import com.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SpuImagesService imagesService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private AttrGroupService groupService;

    @Autowired
    private SkuSaleAttrValueService attrValueService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
//        key:
//        catelogId: 225
//        brandId: 9
//        min: 0
//        max: 0
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            wrapper.ge("price", min);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal(0)) == 1) {
                    wrapper.le("price", max);
                }
            } catch (Exception e) {

            }

        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Override
    public SkuItemVo getItemInfo(Long skuId) {

        SkuItemVo skuItemVo = new SkuItemVo();

//        skuItemVo.setHasStock();
//        调用getHasStock订单服务查询是否还有库存

        SkuInfoEntity skuInfoEntity = skuInfoService.getOne(new QueryWrapper<SkuInfoEntity>().eq("sku_id", skuId));
        skuItemVo.setInfo(skuInfoEntity);

        Long spuId = skuInfoEntity.getSpuId();
        List<SpuImagesEntity> images = imagesService.getBySkuId(spuId);
        skuItemVo.setImages(images);

        Long catalogId = skuInfoEntity.getCatalogId();
        SpuInfoDescEntity descEntity = spuInfoDescService.getOne(new QueryWrapper<SpuInfoDescEntity>().eq("spu_id", spuId));
        skuItemVo.setDesp(descEntity);

        //组合封装sku销售分组信息
        List<SkuItemVo.SpuItemAttrGroupVo> vo =groupService.getAttrGroupWithAttrsBySpuIdAndCategoryId(spuId,catalogId);
        skuItemVo.setGroupAttrs(vo);
        //组合封装sku销售版本信息
        List<SkuItemVo.SkuItemSaleAttrVo> attrValueVos= attrValueService.getSkuSaleAttrValueBySpuId(spuId);
        skuItemVo.setSaleAttr(attrValueVos);
        return skuItemVo;
    }

}