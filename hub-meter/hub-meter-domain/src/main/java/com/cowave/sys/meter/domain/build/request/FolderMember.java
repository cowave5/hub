package com.cowave.sys.meter.domain.build.request;

import com.cowave.sys.meter.domain.constants.FolderRole;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class FolderMember {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户角色
     */
    private FolderRole userRole;
}
