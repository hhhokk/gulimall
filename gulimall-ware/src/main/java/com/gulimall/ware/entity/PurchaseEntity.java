package com.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购信息
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 22:27:23
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     *
     */
    private Long assigneeId;
    /**
     *
     */
    private String assigneeName;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private Integer priority;
    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private Long wareId;
    /**
     *
     */
    private BigDecimal amount;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;

}
