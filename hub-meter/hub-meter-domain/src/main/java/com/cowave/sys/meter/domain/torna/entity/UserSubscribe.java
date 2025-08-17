package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：user_subscribe
 * 备注：用户订阅表
 *
 * @author tanghc
 */
@Data
public class UserSubscribe {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** user_info.id, 数据库字段：user_id */
    private Long userId;

    /** 类型，1：订阅接口，2：订阅项目, 数据库字段：type */
    private Byte type;

    /** 关联id，当type=1对应doc_info.id；type=2对应project.id, 数据库字段：source_id */
    private Long sourceId;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
