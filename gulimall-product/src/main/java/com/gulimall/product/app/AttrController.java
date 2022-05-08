package com.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.gulimall.product.vo.AttrRespVo;
import com.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.product.service.AttrService;
import com.gulimall.common.utils.PageUtils;
import com.gulimall.common.utils.R;



/**
 * 商品属性
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:10:32
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    //localhost:88/api/product/attr/base/list/
    //localhost:88/api/product/attr/sale/list/
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String attrType){

        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,attrType);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     *
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attr = attrService.getAttrRespVo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){

		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttrVO(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
