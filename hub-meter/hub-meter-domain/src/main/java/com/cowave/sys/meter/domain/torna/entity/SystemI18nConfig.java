package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：system_i18n_config
 * 备注：国际化配置
 *
 * @author tanghc
 */
@Data
public class SystemI18nConfig {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 语言简写，如:zh,en
     */
    private String lang;


    /**
     * 描述，如：简体中文
     */
    private String description;


    /**
     * 配置项，properties文件内容
     */
    private String content;


    private Byte isDeleted;


    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModified;


}
