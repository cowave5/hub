package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：compose_additional_page
 * 备注：聚合文档附加页
 *
 * @author tanghc
 */
@Data
public class ComposeAdditionalPage {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** compose_project.id, 数据库字段：project_id */
    private Long projectId;

    /** 文档标题, 数据库字段：title */
    private String title;

    /** 文档内容, 数据库字段：content */
    private String content;

    /** 排序值, 数据库字段：order_index */
    private Integer orderIndex;

    /** 1:启用，0：禁用, 数据库字段：status */
    private Byte status;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
