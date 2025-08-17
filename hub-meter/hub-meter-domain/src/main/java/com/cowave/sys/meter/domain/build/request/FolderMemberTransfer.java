package com.cowave.sys.meter.domain.build.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class FolderMemberTransfer {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;
}
