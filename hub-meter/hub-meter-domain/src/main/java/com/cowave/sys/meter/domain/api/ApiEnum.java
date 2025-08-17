package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class ApiEnum {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 目录id
     */
    private Long folderId;

    /**
     * 唯一id，md5(module_id:name)
     */
    private String dataId;

    /**
     * 枚举名称
     */
    private String name;

    /**
     * 枚举说明
     */
    private String description;

    /**
     * 是否删除
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 修改时间
     */
    private Date updateTime = new Date();
}
