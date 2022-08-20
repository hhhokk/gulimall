package com.gulimall.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zy
 * @create 2022-07-18-20:42
 */
@Data
public class Cart implements Serializable {

    List<CartItem> items;

    private Integer countNum; //商品个数
    private Integer countType; // 商品类型个数
    private BigDecimal countPrice; //总价
    private BigDecimal reduce = new BigDecimal(0); //减免的优惠价格

    public BigDecimal getCountPrice() {
        //计算选中的商品总价
        BigDecimal amount = new BigDecimal("0.00");
        if(items != null && items.size() > 0){
            for (CartItem item : items) {
                if(item.getCheck() == true){
                    amount =amount.add(item.getTotalPrice());
                }
            }
        }
        //减去优惠的总价
        amount.subtract(getReduce());
        return amount;
    }

    public Integer getCountNum() {
        int count = 0;
        if(items != null && items.size() > 0){
            for (CartItem item : items) {
                if(item.getCheck() == true){
                    count += item.getCount();
                }
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if(items != null && items.size() > 0){
            for (CartItem item : items) {
                if(item.getCheck() == true){
                    count += 1;
                }
            }
        }
        return count;
    }
}
