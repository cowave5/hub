package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class ApiEnumItem {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 枚举id
     */
    private Long enumId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 枚举值
     */
    private String value;

    /**
     * 枚举描述
     */
    private String description;

    /**
     * 排序索引
     */
    private Integer orderIndex;

    /**
     * 是否删除
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 修改时间
     */
    private Date updateTime = new Date();
}
