package com.cowave.sys.meter.domain.build;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildConfig implements AccessInfoSetter {

    /**
     * 构建id
     */
    private Integer buildId;

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 构建名称
     */
    private String buildName;

    /**
     * 上次构建状态
     */
    private Integer lastStatus;

    /**
     * 上次构建耗时
     */
    private Long lastCost;

    /**
     * 上次构建成功时间
     */
    private Date lastTimeSuccess;

    /**
     * 上次构建失败时间
     */
    private Date lastTimeFailure;

    /**
     * 仓库地址
     */
    private String repoUrl;

    /**
     * 仓库类型
     */
    private String repoType;

    /**
     * 仓库协议
     */
    private String repoProtocol;

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
