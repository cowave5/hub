package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class ApiParam {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上级参数id
     */
    private Long parentId;

    /**
     * 文档id
     */
    private Long apiId;

    /**
     * 枚举id
     */
    private Long enumId;

    /**
     * <p>唯一id
     * <p>md5(doc_id:parent_id:style:name)
     */
    private String dataId;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 是否必须，1：是，0：否
     */
    private Byte required;

    /**
     * 最大长度
     */
    private String maxLength;

    /**
     * 示例值
     */
    private String example;

    /**
     * 描述
     */
    private String description;

    /**
     * 0：path, 1：header， 2：请求参数，3：返回参数，4：错误码
     */
    private Byte style;

    /**
     * 排序索引
     */
    private Integer orderIndex;

    /**
     * 是否删除
     */
    private Byte isDeleted;

    /**
     * 0：人工操作，1：平台推送
     */
    private Byte createMode;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 0：人工操作，1：平台推送
     */
    private Byte updateMode;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改人名称
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateTime = new Date();
}
