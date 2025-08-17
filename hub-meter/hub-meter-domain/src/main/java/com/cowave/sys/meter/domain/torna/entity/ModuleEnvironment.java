package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：module_environment
 * 备注：模块调试环境
 *
 * @author tanghc
 */
@Data
public class ModuleEnvironment {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** module.id, 数据库字段：module_id */
    private Long moduleId;

    /** 环境名称, 数据库字段：name */
    private String name;

    /** 调试路径, 数据库字段：url */
    private String url;

    /** 是否公开, 数据库字段：is_public */
    private Byte isPublic;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
