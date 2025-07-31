package com.cowave.sys.meter.domain.build.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class FolderMemberQuery {

    /**
     * 上级目录id
     */
    private Integer parentId;

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 用户名称
     */
    private String userName;
}
