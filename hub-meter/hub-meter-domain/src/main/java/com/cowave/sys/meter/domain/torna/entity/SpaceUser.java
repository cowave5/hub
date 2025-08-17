package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：space_user
 * 备注：分组用户关系表
 *
 * @author tanghc
 */
@Data
public class SpaceUser {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** user_info.id, 数据库字段：user_id */
    private Long userId;

    /** space.id, 数据库字段：space_id */
    private Long spaceId;

    /** 角色，guest：访客，dev：开发者，admin：空间管理员, 数据库字段：role_code */
    private String roleCode;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
