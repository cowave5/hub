package com.cowave.sys.meter.domain.build;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.sys.meter.domain.constants.FolderRole;
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
public class BuildFolderMember implements AccessInfoSetter {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 成员编码
     */
    private String userCode;

    /**
     * 成员名称
     */
    private String userName;

    /**
     * 成员角色
     */
    private FolderRole userRole;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
