package com.gulimall.common.constant;

/**
 * @author zy
 * @create 2022-03-12-15:56
 */
public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }

    public enum ProductStatusEnum{
        PRODUCT_SPU_NEW(0,"新建状态"),
        PRODUCT_SPU_UP(1,"商品上架"),
        PRODUCT_SPU_DOWN(2,"商品下架");
        private int code;
        private String msg;

        ProductStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
    }
}
