package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 表名：ms_module_config
 * 备注：MeterSphere模块配置
 *
 * @author thc
 */
@Data
public class MsModuleConfig {

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * module.id
     */
    private Long moduleId;

    /**
     * project_release.id
     */
    private Long releaseId;

    /**
     * 默认覆盖
     */
    private Byte msCoverModule;

    /**
     * MeterSphere模块id
     */
    private String msModuleId;

    /**
     * MeterSphere项目id
     */
    private String msProjectId;

    private String name;


}
