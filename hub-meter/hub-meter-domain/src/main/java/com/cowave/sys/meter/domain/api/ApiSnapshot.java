package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class ApiSnapshot {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 文档内容
     */
    private String content;

    /**
     * 文档md5
     */
    private String md5;

    /**
     * <p>唯一id
     * <p>接口规则：md5(module_id:parent_id:url:http_method)
     * <p>分类规则：md5(module_id:parent_id:name)
     */
    private String docKey;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 修改时间
     */
    private Date updateTime = new Date();
}
