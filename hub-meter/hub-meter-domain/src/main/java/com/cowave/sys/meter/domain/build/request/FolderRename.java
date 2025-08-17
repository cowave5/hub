package com.cowave.sys.meter.domain.build.request;

import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class FolderRename {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 目录名称
     */
    private String folderName;
}
