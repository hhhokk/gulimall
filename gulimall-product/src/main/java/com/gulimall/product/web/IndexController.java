package com.gulimall.product.web;

import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.service.CategoryService;
import com.gulimall.product.vo.Catelog2LevelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zy
 * @create 2022-05-06-22:25
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        //TODO 查出一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevelOneCategory();
        model.addAttribute("categorys",categoryEntityList);
        return "index";
    }

    @ResponseBody
    @GetMapping({"/index/catalog.json"})
    public Map<String, List<Catelog2LevelVo>> getCatelogJson(){
        Map<String, List<Catelog2LevelVo>> map = categoryService.getCatelogJson();
        return map;

    }
}
