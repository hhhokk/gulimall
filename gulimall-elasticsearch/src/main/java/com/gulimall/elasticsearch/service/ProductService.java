package com.gulimall.elasticsearch.service;


import com.gulimall.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author zy
 * @create 2022-04-26-22:10
 */

public interface ProductService {
    Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
