package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class ApiFolder {

    /**
     * 目录id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 目录名称
     */
    private String name;

    private Long projectId;

    /** 模块类型，0：自定义添加，1：swagger导入，2：postman导入, 数据库字段：type */
    private Byte type;

    /**
     * 导入url
     */
    private String importUrl;

    /**
     * basic认证用户名
     */
    private String basicUsername;

    /**
     * basic认证密码
     */
    private String basicPassword;

    /**
     * 开放调用token
     */
    private String token;

    /**
     * 排序索引
     */
    private Integer orderIndex;

}
