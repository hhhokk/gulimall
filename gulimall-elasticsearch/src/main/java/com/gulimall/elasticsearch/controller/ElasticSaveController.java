package com.gulimall.elasticsearch.controller;

import com.gulimall.common.exception.BizCodeEnum;
import com.gulimall.common.to.es.SkuEsModel;
import com.gulimall.common.utils.R;
import com.gulimall.elasticsearch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author zy
 * @create 2022-04-26-21:29
 */
@RestController
@RequestMapping("/search/save")
@Slf4j
public class ElasticSaveController {

    @Autowired
    private ProductService productService;
    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try {
            b = productService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误:{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }
        if (b) {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMessage());
        }else {
            return R.ok();
        }

    }
}
