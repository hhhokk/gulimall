package com.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gulimall.common.constant.ProductConstant;
import com.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.gulimall.product.entity.AttrGroupEntity;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.service.AttrAttrgroupRelationService;
import com.gulimall.product.service.AttrGroupService;
import com.gulimall.product.service.CategoryService;
import com.gulimall.product.vo.AttrGroupRelationVo;
import com.gulimall.product.vo.AttrRespVo;
import com.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.Query;

import com.gulimall.product.dao.AttrDao;
import com.gulimall.product.entity.AttrEntity;
import com.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrgroupRelationService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    AttrAttrgroupRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attr,entity);
        //1.保存基本数据
        baseMapper.insert(entity);
        //保存关联关系
        if(attr.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrgroupRelationService.save(relationEntity);
        }

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type","base".equalsIgnoreCase(attrType)? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId != 0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrRespVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分类和分组的名字
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrgroupRelationEntity attrId = attrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null) {
                    AttrGroupEntity entity = attrGroupService.getById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(entity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrRespVo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);
        AttrAttrgroupRelationEntity attrgroupOne = attrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));

        if(attrEntity.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //设置分组信息
            if(attrgroupOne != null){
                attrRespVo.setAttrGroupId(attrgroupOne.getAttrGroupId());
                AttrGroupEntity groupEntity = attrGroupService.getById(attrgroupOne.getAttrGroupId());
                if(groupEntity != null){
                    attrRespVo.setGroupName(groupEntity.getAttrGroupName());
                }
            }
        }


        //设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if(categoryEntity !=null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttrVO(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        baseMapper.updateById(attrEntity);

        if(attrEntity.getAttrType()==ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {

            //修改关联关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());

            attrgroupRelationService.saveOrUpdate(relationEntity,
                    new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        }
    }

    /**
     * 根据分组id找到所有关联的所有属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> queryAttrRelation(Long attrgroupId) {
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId);
        List<AttrAttrgroupRelationEntity> list = attrgroupRelationService.list(queryWrapper);
//        List<Long> longList = list.stream().map((item) -> {
//            return item.getAttrId();
//        }).collect(Collectors.toList());
//        if(longList == null || longList.size() ==0){
//            return null;
//        }

        List<Long> attrIds = new ArrayList<>();
        if(attrIds == null || attrIds.size() ==0){
            return null;
        }
        for (AttrAttrgroupRelationEntity entity : list) {
            attrIds.add(entity.getAttrId());
        }
        List<AttrEntity> attrEntities = baseMapper.selectBatchIds(attrIds);

        return attrEntities;
    }

    /**
     * 根据分组id删除所有关联的所有属性
     * @param vos
     * @return
     */
    @Override
    public void relationDelete(AttrGroupRelationVo[] vos) {
        ArrayList<AttrAttrgroupRelationEntity> attrEntities = new ArrayList<>();
        for (AttrGroupRelationVo vo : Arrays.asList(vos)) {
            AttrAttrgroupRelationEntity attrEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo,attrEntity);
            attrEntities.add(attrEntity);
        }
        /*
        流式编写
        List<AttrAttrgroupRelationEntity> attrEntities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity attrEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrEntity);
            return attrEntity;
        }).collect(Collectors.toList());

        */

        relationDao.removeBatchList(attrEntities);
    }


}