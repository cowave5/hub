package com.cowave.sys.meter.domain.torna.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表名：user_wecom_info
 * 备注：企业微信开放平台用户
 *
 * @author Lin
 * @date 2023-11-29  17:02:14
 */
@Data
public class UserWeComInfo {

    /**  数据库字段：id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**  企业微信绑定手机号码：mobile */
    private String mobile;

    /** user_info.id, 数据库字段：user_info_id */
    private Long userInfoId;

    /**  数据库字段：gmt_create */
    private LocalDateTime gmtCreate;

        /**  数据库字段：gmt_modified */
    private LocalDateTime gmtModified;
}
