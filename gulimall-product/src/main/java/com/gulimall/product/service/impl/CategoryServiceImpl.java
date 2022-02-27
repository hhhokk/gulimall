package com.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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



}