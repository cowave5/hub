package com.cowave.sys.meter.domain.torna.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author tanghc
 */
@Data
public class SystemLoginToken {

    private Long id;

    private String loginKey;

    private String token;

    private LocalDateTime expireTime;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

    /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;

}
