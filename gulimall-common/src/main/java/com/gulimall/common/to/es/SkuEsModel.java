package com.gulimall.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zy
 * @create 2022-04-24-21:47
 */

@Data
public class SkuEsModel {

    private String brandImg;
    private Long brandId;
    private Long categoryId;
    private Long hotScore;
    private Long saleCount;
    private Long skuId;
    private Long spuId;
    private String brandName;
    private String categoryName;
    private String skuImg;
    private BigDecimal skuPrice;
    private String skuTitle;
    private Boolean hasStock;
    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
