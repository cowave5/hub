package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：share_content
 * 备注：分享详情
 *
 * @author tanghc
 */
@Data
public class ShareContent {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** share_config.id, 数据库字段：share_config_id */
    private Long shareConfigId;

    /** 文档id, 数据库字段：doc_id */
    private Long docId;

    /** 父id, 数据库字段：parent_id */
    private Long parentId;

    /** 是否分享整个分类, 数据库字段：is_share_folder */
    private Byte isShareFolder;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
