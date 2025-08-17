package com.cowave.sys.meter.domain.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author tanghc
 */
@Data
public class ApiDefinition {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 是否分组，0：不是，1：是
     */
    private Byte isGroup;

    /**
     * 目录id
     */
    private Long folderId;

    /**
     * <p>数据id
     * <p>接口规则：md5(module_id:parent_id:url:http_method:version)
     * <p>分类规则：md5(module_id:parent_id:name)
     */
    private String dataId;

    /**
     * <p>唯一id
     * <p>接口规则：md5(module_id:parent_id:url:http_method)
     * <p>分类规则：md5(module_id:parent_id:name)
     */
    private String docKey;

    /**
     * 文档内容的md5值
     */
    private String md5;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 维护人
     */
    private String author;

    /**
     * 0:http,1:dubbo
     */
    private Byte type;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 版本号
     */
    private String version;

    /**
     * http方法
     */
    private String httpMethod;

    /**
     * contentType
     */
    private String contentType;

    /**
     * 废弃信息
     */
    private String deprecated;

    /**
     * 是否使用全局请求头
     */
    private Byte isUseGlobalHeaders;

    /**
     * 是否使用全局请求参数
     */
    private Byte isUseGlobalParams;

    /**
     * 是否使用全局返回参数
     */
    private Byte isUseGlobalReturns;

    /**
     * 是否请求数组
     */
    private Byte isRequestArray;

    /**
     * 是否返回数组
     */
    private Byte isResponseArray;

    /**
     * 请求数组时元素类型
     */
    private String requestArrayType;

    /**
     * 返回数组时元素类型
     */
    private String responseArrayType;

    /**
     * 排序索引
     */
    private Integer orderIndex;

    /**
     * 文档状态
     */
    private Byte status;

    /**
     * 是否显示
     */
    private Byte isShow;

    /**
     * 是否锁住
     */
    private Byte isLocked;

    /**
     * 是否删除
     */
    private Byte isDeleted;

    /**
     * 备注
     */
    private String remark;

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
