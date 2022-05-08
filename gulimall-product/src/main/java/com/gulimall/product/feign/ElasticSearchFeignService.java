package com.gulimall.product.feign;

import com.gulimall.common.to.es.SkuEsModel;
import com.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author zy
 * @create 2022-04-26-22:55
 */
@FeignClient("gulimall-elasticsearch")
public interface ElasticSearchFeignService {

    //上架商品
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
