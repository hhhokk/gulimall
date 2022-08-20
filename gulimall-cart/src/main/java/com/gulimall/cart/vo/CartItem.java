package com.gulimall.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zy
 * @create 2022-07-18-20:44
 */
@Data
public class CartItem implements Serializable {

    private String image;
    private Long skuId;
    private BigDecimal price; //单价
    private Integer count;
    private Boolean check = true;
    private String title;
    private List<String> skuAttr;
    private BigDecimal totalPrice; //总价

    public BigDecimal getTotalPrice() {
        BigDecimal multiply = this.price.multiply(new BigDecimal("" + this.count));
        return multiply;
    }
}
