package com.gulimall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;
import com.gulimall.product.dao.SpuImagesDao;
import com.gulimall.product.entity.SpuImagesEntity;
import com.gulimall.product.service.SpuImagesService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long id, List<String> images) {
        if (CollectionUtils.isEmpty(images)) {

        } else {
            List<SpuImagesEntity> list = images.stream().map(item -> {
                SpuImagesEntity entity = new SpuImagesEntity();
                entity.setSpuId(id);
                entity.setImgUrl(item);
//                BeanUtils.copyProperties(item, entity);
                return entity;
            }).collect(Collectors.toList());
            this.saveBatch(list);
        }

    }

    @Override
    public List<SpuImagesEntity> getBySkuId(Long spuId) {
        List<SpuImagesEntity> images = baseMapper.selectList(new QueryWrapper<SpuImagesEntity>().eq("spu_id", spuId));
        return images;
    }

}