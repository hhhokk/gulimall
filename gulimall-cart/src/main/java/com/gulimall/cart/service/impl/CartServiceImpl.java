package com.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.gulimall.cart.feign.ProductInfoService;
import com.gulimall.cart.interceptor.CartInterceptor;
import com.gulimall.cart.service.CartService;
import com.gulimall.cart.vo.Cart;
import com.gulimall.cart.vo.CartItem;
import com.gulimall.cart.vo.UserInfoTo;
import com.gulimall.common.utils.R;
import com.gulimall.common.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author zy
 * @create 2022-07-24-19:38
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String CART_PREFIX = "gulimall:cart:";

    @Autowired
    ProductInfoService productInfoService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        CartItem cartItem;
        if (res == null) { //判断购物车是否有该购物项，如果没有则新增
            cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                R info = productInfoService.info(skuId);
                Map data = (HashMap) info.get("skuInfo");
                SkuInfoVo skuInfo = JSON.parseObject(JSON.toJSONString(data), SkuInfoVo.class);
                cartItem.setCount(num);
                cartItem.setPrice(skuInfo.getPrice());
                cartItem.setImage(skuInfo.getSkuDefaultImg());
                cartItem.setCheck(true);
                cartItem.setTitle(skuInfo.getSkuTitle());
                cartItem.setSkuId(skuId);
            }, executor);
            CompletableFuture<Void> saleAttrValue = CompletableFuture.runAsync(() -> {
                List<String> strings = productInfoService.saleAtterValue(skuId);
                cartItem.setSkuAttr(strings);
            }, executor);
            CompletableFuture.allOf(getSkuInfoTask, saleAttrValue).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), s);
        } else {
            //如果有购物项则增加数目
            cartItem = JSON.parseObject(res, CartItem.class);
            Integer count = cartItem.getCount();
            cartItem.setCount(count + num);
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), s);
        }
        return cartItem;

    }

    /**
     * 获取购物车的莫一项
     *
     * @param skuId
     * @return
     */
    public CartItem getToCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(res, CartItem.class);
        return cartItem;
    }

    public BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (!StringUtils.isEmpty(userInfoTo.getUserId())) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        return hashOps;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        String tempCartKey;
        if (userInfoTo.getUserId() != null) { //已经登录
            tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItem = getCartItems(tempCartKey);
            if (tempCartItem != null && tempCartItem.size() > 0) {
                for (CartItem cartItem : tempCartItem) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
            }
            clearCart(tempCartKey);
            List<CartItem> cartItem = getCartItems(CART_PREFIX + userInfoTo.getUserId());
            cart.setItems(cartItem);
        } else { //没登陆
            tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItem = getCartItems(tempCartKey);
            cart.setItems(cartItem);
        }
        return cart;

    }

    /**
     * 获取购物车的所有购物项
     *
     * @return
     */
    public List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        List<CartItem> collect = null;
        if (values != null && values.size() > 0) {
            collect = values.stream().map(obj -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
        }
        return collect;
    }

    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        CartItem cartItem = getToCartItem(skuId);
        cartItem.setCheck(check == 1);
        String s = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), s);
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        if (num == 0) {
            deleteItem(skuId);
        } else {
            CartItem cartItem = getToCartItem(skuId);
            cartItem.setCount(num);
            String s = JSON.toJSONString(cartItem);
            BoundHashOperations<String, Object, Object> cartOps = getCartOps();
            cartOps.put(skuId.toString(), s);
        }

    }
}
