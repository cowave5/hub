package com.cowave.sys.meter.domain.env;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.sys.meter.domain.constants.CredentialScope;
import com.cowave.sys.meter.domain.constants.CredentialType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class EnvCredential implements AccessInfoSetter {

    /**
     * 凭据id
     */
    @TableId(type = IdType.AUTO)
    private Integer credentialId;

    /**
     * 凭据名称
     */
    private String credentialName;

    /**
     * 凭据类型
     */
    private CredentialType credentialType;

    /**
     * 使用范围
     */
    private CredentialScope credentialScope;

    /**
     * 名称
     */
    private String username;

    /**
     * 密码
     */
    private String secret;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
