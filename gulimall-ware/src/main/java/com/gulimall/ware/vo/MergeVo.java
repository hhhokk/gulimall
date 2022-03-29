package com.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zy
 * @create 2022-03-29-20:53
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
