package com.gulimall.product.vo;

import com.gulimall.product.entity.SkuImagesEntity;
import com.gulimall.product.entity.SkuInfoEntity;
import com.gulimall.product.entity.SpuImagesEntity;
import com.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author zy
 * @create 2022-06-03-22:32
 */
@Data
public class SkuItemVo {

    private boolean hasStock = true;

    private SkuInfoEntity info;

    private SpuInfoDescEntity desp;

    private List<SkuImagesEntity> images;

    private List<SkuItemSaleAttrVo> saleAttr;

    private List<SpuItemAttrGroupVo> groupAttrs;


    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> baseAttrVos;
    }

    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }

    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;

    }

    @Data
    public static class AttrValueWithSkuIdVo {
        private String skuIds;
        private String attrValue;

    }
}
