package com.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zy
 * @create 2022-03-27-13:12
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
