package com.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gulimall.common.valid.AddGroup;
import com.gulimall.common.valid.ListValue;
import com.gulimall.common.valid.UpdateGroup;
import com.gulimall.common.valid.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 *
 * @author zy
 * @email zy@gmail.com
 * @date 2022-02-18 21:10:32
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @NotNull(message = "必须指定品牌id",groups = {UpdateGroup.class})
	@Null(message = "新增不能指定id",groups = {AddGroup.class})
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空",groups = {UpdateGroup.class,AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */

    @NotEmpty(groups = {AddGroup.class,UpdateGroup.class})
    @URL(groups = {UpdateGroup.class,AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     *
     * 自定义注解，showStatus的值只能为0，1
     */
    @NotNull(groups = {UpdateGroup.class,AddGroup.class, UpdateStatusGroup.class})
    @ListValue(vals = {0,1},groups = {UpdateGroup.class,AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(groups = {UpdateGroup.class,AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "必须是一个字母",groups = {UpdateGroup.class,AddGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = {UpdateGroup.class,AddGroup.class})
    @Min(value = 0, message = "必须是一个不小于0的整数",groups = {UpdateGroup.class,AddGroup.class})
    private Integer sort;

}
