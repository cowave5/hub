package com.cowave.sys.meter.domain.build.request;

import com.cowave.sys.meter.domain.constants.FolderRole;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class FolderMemberUpdate {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户角色
     */
    private FolderRole userRole;
}
