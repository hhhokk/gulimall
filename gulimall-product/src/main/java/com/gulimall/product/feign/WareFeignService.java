package com.gulimall.product.feign;

import com.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 * @author zy
 * @create 2022-04-25-22:42
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("ware/waresku/hasstock")
    R getHasStock(@RequestBody List<Long> skuIds);
}
