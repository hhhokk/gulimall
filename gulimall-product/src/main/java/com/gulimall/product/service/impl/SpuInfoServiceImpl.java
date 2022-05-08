package com.gulimall.product.service.impl;

import com.gulimall.common.constant.ProductConstant;
import com.gulimall.common.to.SkuReductionTo;
import com.gulimall.common.to.SpuBoundTo;
import com.gulimall.common.to.es.SkuEsModel;
import com.gulimall.common.utils.R;
import com.gulimall.product.entity.*;
import com.gulimall.product.feign.CouponFeignService;
import com.gulimall.product.feign.ElasticSearchFeignService;
import com.gulimall.product.feign.WareFeignService;
import com.gulimall.product.service.*;
import com.gulimall.product.vo.spusavevo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;
import com.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService descService;

    @Autowired
    private SpuImagesService imagesService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService saleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private ElasticSearchFeignService searchFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        this.saveBaseSpuInfo(infoEntity);

        //2.保存spu的描述图片pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        /*
        * String.join(",",decript),list里面的数据用","拼接起来
        */
        descEntity.setDecript(String.join(",",decript));
        descService.saveBaseSpuInfoDesc(descEntity);

        //3.保存spu的图片集pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(infoEntity.getId(),images);

        //4.保存spu的规格参数pms_product_attr_value
        List<BaseAttrs> baseAttrsList = vo.getBaseAttrs();
        List<ProductAttrValueEntity> valueEntityList = baseAttrsList.stream().map(item -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setSpuId(infoEntity.getId());
            AttrEntity attrEntity = attrService.getById(item.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setAttrId(item.getAttrId());
            valueEntity.setQuickShow(item.getShowDesc());
            valueEntity.setAttrValue(item.getAttrValues());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveBatch(valueEntityList);

        //5.保存spu的积分信息gulimall-sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode() != 0){
            log.error("远程保存spu积分信息失败");
        }

        //6.保存当前spu对应的所有sku信息
        //6.1）sku的基本信息 pms_sku_info
//        private String skuName;
//        private BigDecimal price;
//        private String skuTitle;
//        private String skuSubtitle;

        List<Skus> skus = vo.getSkus();
        if(!CollectionUtils.isEmpty(skus)){

            skus.forEach(item ->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg()==1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSaleCount(0L);
                skuInfoService.save(skuInfoEntity);


                //6.2）sku的图片信息pms_sku_images
                List<SkuImagesEntity> collect = item.getImages().stream().map(imag -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    BeanUtils.copyProperties(imag, imagesEntity);
                    imagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return imagesEntity;
                }).filter(entity ->{
                    //返回true就是需要，false就是剔除掉
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(collect);
                //TODO 没有图片路径的无需保存
                //6.3）sku的销售属性信息pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity saleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, saleAttrValueEntity);
                    saleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return saleAttrValueEntity;
                }).collect(Collectors.toList());
                saleAttrValueService.saveBatch(saleAttrValueEntities);

                //6.4）sku的优惠，满减等信息gulimall_sms->sms_sku_ladder\sms_sku_full
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }

            });
        }
    }

    /**
     * //1.保存spu基本信息 pms_spu_info
     * @param infoEntity
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
    baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w ->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void up(Long spuId) {
        //查询当前sku所有可以被检索的规格属性 attrs
        //查出当前sku所有的属性
        List<ProductAttrValueEntity> productAttrValueEntityList =
                productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> collect = productAttrValueEntityList.stream()
                .map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        //从所有attr中排除不可检索的属性
        List<AttrEntity> attrEntities = attrService.listByIds(collect);

        List<SkuEsModel.Attrs> attrsList = attrEntities.stream().filter(attrEntity -> {
            return attrEntity.getSearchType() != 0;
        }).map(attrEntity ->{
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(attrEntity,attrs);
            attrs.setAttrValue(attrEntity.getValueSelect());
            return attrs;
        }).collect(Collectors.toList());


        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.getSkuBySpuId(spuId);
        // 查询hasStock是否还有库存
        List<Long> skuIds = skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<String,Boolean> data = null;
        try{
            R hasStock = wareFeignService.getHasStock(skuIds);
            data = (Map<String, Boolean>) hasStock.getData();
        }catch (Exception e){
            log.error("库存查询服务异常,原因{}",e);
        }
        Map<String, Boolean> finalData = data;

        List<SkuEsModel> upProducts = skuInfoEntityList.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,skuEsModel);
            skuEsModel.setCategoryId(sku.getCatalogId());
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            //  hotScore  hasStock  categoryName brandName brandImg

            // 查询hasStock是否还有库存,先默认赋值为false
            skuEsModel.setHasStock(false);

            if (finalData != null && finalData.size() !=0){
                Boolean aBoolean = finalData.get(String.valueOf(sku.getSkuId()));
                skuEsModel.setHasStock(aBoolean);
                finalData.remove(String.valueOf(sku.getSkuId()));
            }



            // 热度评分，默认0.
            skuEsModel.setHotScore(0L);
            skuEsModel.setCategoryName(categoryService.getById(sku.getCatalogId()).getName());
            BrandEntity byId = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(byId.getName());
            skuEsModel.setBrandImg(byId.getLogo());
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());
        R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode() == 0 ){
            //远程调用成功,修改上架的状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.ProductStatusEnum.PRODUCT_SPU_UP.getCode());
        }
    }

}