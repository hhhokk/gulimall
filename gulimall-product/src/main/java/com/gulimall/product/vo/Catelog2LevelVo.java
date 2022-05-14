package com.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zy
 * @create 2022-05-08-17:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2LevelVo {

    private String catalog1Id;
    private String id;
    private String name;
    private List<Catelog3LevelVo> catalog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3LevelVo {
        private String catalog2Id;
        private String id;
        private String name;
    }

}
