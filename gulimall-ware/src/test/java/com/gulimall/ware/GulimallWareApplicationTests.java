package com.gulimall.ware;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gulimall.ware.entity.WareSkuEntity;
import com.gulimall.ware.service.WareSkuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallWareApplicationTests {

    @Autowired
    WareSkuService wareSkuService;
    @Test
    public void contextLoads() {
        List<String> skuIds = new ArrayList<>();
        skuIds.add("2");
        skuIds.add("3");
        skuIds.add("4");
        List<WareSkuEntity> wareSkuEntities = skuIds.stream().map(skuId -> {
            return wareSkuService.getOne(new QueryWrapper<WareSkuEntity>().eq("sku_id",skuId));
        }).collect(Collectors.toList());
        System.out.println(wareSkuEntities);
    }

}
