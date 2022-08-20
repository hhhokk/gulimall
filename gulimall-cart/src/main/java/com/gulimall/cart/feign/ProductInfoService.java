package com.gulimall.cart.feign;

import com.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zy
 * @create 2022-08-17-0:37
 */
@FeignClient("gulimall-product")
public interface ProductInfoService {

    @RequestMapping("product/skuinfo/info/{skuId}")
//    @RequiresPermissions("product:skuinfo:info")
    R info(@PathVariable("skuId") Long skuId);


    @GetMapping("product/skusaleattrvalue/stringList/{skuId}")
    List<String> saleAtterValue(@PathVariable("skuId") Long skuId);
}
