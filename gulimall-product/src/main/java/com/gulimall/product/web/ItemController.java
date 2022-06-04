package com.gulimall.product.web;
import com.gulimall.product.service.SkuInfoService;
import com.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zy
 * @create 2022-06-03-19:48
 */
@Controller()
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){
        SkuItemVo vo = skuInfoService.getItemInfo(skuId);
        System.out.println(vo);
        model.addAttribute("item",vo);
        return "item";
    }
}
