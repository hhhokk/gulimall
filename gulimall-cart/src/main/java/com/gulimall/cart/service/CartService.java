package com.gulimall.cart.service;

import com.gulimall.cart.vo.Cart;
import com.gulimall.cart.vo.CartItem;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.concurrent.ExecutionException;

/**
 * @author zy
 * @create 2022-07-24-19:38
 */
public interface CartService {
    /**
     * 添加某一项到购物车
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车的某个购物项
     * @param skuId
     * @return
     */
    CartItem getToCartItem(Long skuId);

    BoundHashOperations<String, Object, Object> getCartOps();

    /**
     * 获取整个购物车
     * @return
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     * @param cartKey
     */
    void clearCart(String cartKey);

    /**
     * 判断是否选中得状态
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 删除某个购物项
     * @param skuId
     */
    void deleteItem(Long skuId);

    /**
     * 改变购物车数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);
}
