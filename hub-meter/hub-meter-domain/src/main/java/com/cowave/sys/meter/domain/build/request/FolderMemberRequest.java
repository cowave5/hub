package com.cowave.sys.meter.domain.build.request;

import lombok.Data;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class FolderMemberRequest {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 目录名称
     */
    private String folderName;

    /**
     * 成员列表
     */
    private List<FolderMember> members;

}
