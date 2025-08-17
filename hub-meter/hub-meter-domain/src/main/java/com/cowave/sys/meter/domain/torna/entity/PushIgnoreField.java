package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：push_ignore_field
 * 备注：推送忽略字段
 *
 * @author tanghc
 */
@Data
public class PushIgnoreField {

    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * module.id
     */
    private Long moduleId;


    /**
     * doc_info.data_id
     */
    private String dataId;


    /**
     * doc_info.name
     */
    private String fieldName;


    /**
     * 字段描述
     */
    private String fieldDescription;


    private LocalDateTime gmtCreate;



}
