package com.gulimall.product.service.impl;

import com.gulimall.product.vo.Catelog2LevelVo;
import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Service;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;

import com.gulimall.product.dao.CategoryDao;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //组装成父子结构
        //找到所有一级分类
        List<CategoryEntity> entityList = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            if(entity.getParentCid()==0){
                entityList.add(entity);
            }
        }
        //遍历一级节点的id与子节点的父id比较
        for (CategoryEntity entity : entityList) {
            //调用递归
            List<CategoryEntity> childrenList = getChildren(entity.getCatId().toString(),entities);
            entity.setChildren(childrenList);
        }


        return entityList;

    }

    public List<CategoryEntity> getChildren(String id,List<CategoryEntity> allMenu) {
        //子菜单
        List<CategoryEntity> childrenList = new ArrayList<>();
        for (CategoryEntity entity : allMenu) {
            if(entity.getParentCid().toString().equals(id)){
                childrenList.add(entity);
            }
        }
        //递归查询
        for (CategoryEntity entity : childrenList) {
            //传入该节点的id与所有节点的父id比较
            entity.setChildren(getChildren(entity.getCatId().toString(),allMenu));
        }
        //如果没有子节点，返回一个空list,递归退出。
        if(childrenList.size()==0){
            return new ArrayList<>();
        }
        return childrenList;
    }

    @Override
    public void removeByIdList(List<Long> asList) {
        // TODO: 2022/2/26 检查当前删除的菜单，是否被别的地方引用
        //用的不多，多会使用逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long attrGroupId) {
        List<Long> longs = new ArrayList<>();
        List<Long> catelogPath = findCatelogPath(attrGroupId, longs);
        Collections.reverse(catelogPath);
        return catelogPath.toArray(new Long[longs.size()]);
    }

    private List<Long> findCatelogPath(Long attrGroupId,List<Long> paths){
        paths.add(attrGroupId);
        Long parentCid = baseMapper.selectById(attrGroupId).getParentCid();
        if(parentCid != 0){
            findCatelogPath(parentCid, paths);
        }

        return paths;
    }


    @Override
    public List<CategoryEntity> getLevelOneCategory() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catelog2LevelVo>> getCatelogJson() {
        List<CategoryEntity> levelOneCategory = getLevelOneCategory(); //查出一级分类

        //查出二级分类
        List<CategoryEntity> category2Entities = baseMapper
                .selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 2));

        //查出三级分类
        List<CategoryEntity> category3Entities = baseMapper
                .selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 3));

        //封装成三级分类vo
        List<Catelog2LevelVo.Catelog3LevelVo> catelog3LevelVoList = category3Entities.stream().map(item -> {
            Catelog2LevelVo.Catelog3LevelVo catelog3LevelVo = new Catelog2LevelVo.Catelog3LevelVo();
            catelog3LevelVo.setId(item.getCatId().toString());
            catelog3LevelVo.setCatalog2Id(item.getParentCid().toString());
            catelog3LevelVo.setName(item.getName());
            return catelog3LevelVo;
        }).collect(Collectors.toList());

        //封装成二级分类vo
        List<Catelog2LevelVo> catelog2LevelVoList = category2Entities.stream().map(item -> {
            ArrayList<Catelog2LevelVo.Catelog3LevelVo> catelog3LevelVosList = new ArrayList<>();
            for (Catelog2LevelVo.Catelog3LevelVo catelog3LevelVo : catelog3LevelVoList) {
                if (Long.valueOf(catelog3LevelVo.getCatalog2Id()) == item.getCatId()) {
                    catelog3LevelVosList.add(catelog3LevelVo);
                }
            }
            Catelog2LevelVo catelog2LevelVo = new Catelog2LevelVo();
            catelog2LevelVo.setId(item.getCatId().toString());
            catelog2LevelVo.setCatalog1Id(item.getParentCid().toString());
            catelog2LevelVo.setName(item.getName());
            catelog2LevelVo.setCatalog3List(catelog3LevelVosList);

            return catelog2LevelVo;
        }).collect(Collectors.toList());

        //返回对应的map
        Map<String, List<Catelog2LevelVo>> map = levelOneCategory.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<Catelog2LevelVo> collect = catelog2LevelVoList.stream().filter(item -> {
                return Long.valueOf(item.getCatalog1Id()) == v.getCatId();
            }).collect(Collectors.toList());
            return collect;
        }));//对应的父id
        return map;
    }


}