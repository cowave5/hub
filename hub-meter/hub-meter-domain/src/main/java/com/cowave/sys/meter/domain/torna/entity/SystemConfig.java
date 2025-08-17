package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：system_config
 * 备注：系统配置表
 *
 * @author tanghc
 */
@Data
public class SystemConfig {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**  数据库字段：config_key */
    private String configKey;

    /**  数据库字段：config_value */
    private String configValue;

    /**  数据库字段：remark */
    private String remark;

    /**  数据库字段：is_deleted */
    private Byte isDeleted;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;


}
