package com.gulimall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gulimall.common.to.es.SkuEsModel;
import com.gulimall.product.entity.AttrEntity;
import com.gulimall.product.entity.BrandEntity;
import com.gulimall.product.service.*;
import com.gulimall.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService service;

    @Autowired
    AttrService attrService;


    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Test
    public void redisson() {
        System.out.println(redissonClient);
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = service.findCatelogPath(225L);
        log.info("{}", Arrays.asList(catelogPath));
    }

    @Test
    public void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("xiaomi");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(item -> {
            System.out.println(item);
        });

    }

    @Test
    public void test() {
        int[] arrs = new int[10];
        for (int i = 0; i < 10; i++) {
            arrs[i] = (int) (Math.random() * 100);
        }
        for (int i = 0; i < arrs.length; i++) {
//            System.out.print(arrs[i]+"  ");
        }
        Arrays.sort(arrs);
        List list = Arrays.stream(arrs).boxed().collect(Collectors.toList());
//        System.out.println(list);
        List list1 = IntStream.of(arrs).boxed().collect(Collectors.toList());
//        System.out.println(list1);

//        list.forEach(item ->
//                System.out.println(item));
        int count = list.size();
        Integer[] ints = (Integer[]) list.toArray(new Integer[count]);

        for (Integer anInt : ints) {
            System.out.print(anInt + " ");
        }

    }

    @Test
    public void test2() {
        ArrayList<String> list = new ArrayList<>();
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("15");
        List<AttrEntity> attrEntities = attrService.listByIds(list);

        List<SkuEsModel.Attrs> attrsList = attrEntities.stream().filter(attrEntity -> attrEntity.getSearchType() != 0)
                .map(attrEntity -> {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(attrEntity, attrs);
                    attrs.setAttrValue(attrEntity.getValueSelect());
                    return attrs;
                }).collect(Collectors.toList());
        System.out.println(attrsList);

    }

    @Test
    public void getAttrGroupWithAttrsBySpuIdAndCategoryIdTest(){
        List<SkuItemVo.SpuItemAttrGroupVo> vos = attrGroupService.getAttrGroupWithAttrsBySpuIdAndCategoryId(13L, 225L);
        log.info("{}",vos);
    }

    @Test
    public void getSkuSaleAttrValueBySpuIdTest(){
        List<SkuItemVo.SkuItemSaleAttrVo> vos = skuSaleAttrValueService.getSkuSaleAttrValueBySpuId(13L);
        log.info("{}",vos);
    }

    @Test
    public void ThreadPoolExecutorTest(){
        String code = String.valueOf(Math.random()*1000000).substring(2,6);
        System.out.println(code);
//        System.out.println(threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
//        System.out.println(threadPoolExecutor.getMaximumPoolSize());
//        log.info("{}",threadPoolExecutor.getMaximumPoolSize());
    }
}
