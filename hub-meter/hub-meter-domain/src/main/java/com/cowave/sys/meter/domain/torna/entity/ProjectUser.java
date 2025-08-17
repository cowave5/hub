package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：project_user
 * 备注：项目用户关系表
 *
 * @author tanghc
 */
@Data
public class ProjectUser {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** project.id, 数据库字段：project_id */
    private Long projectId;

    /** user_info.id, 数据库字段：user_id */
    private Long userId;

    /** 角色，guest：访客，dev：开发者，admin：项目管理员, 数据库字段：role_code */
    private String roleCode;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
