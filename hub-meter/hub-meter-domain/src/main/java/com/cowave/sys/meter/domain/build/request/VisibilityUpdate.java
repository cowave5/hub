package com.cowave.sys.meter.domain.build.request;

import com.cowave.sys.meter.domain.constants.Visibility;
import lombok.Data;

/**
 * @author shanhuiming
 */
@Data
public class VisibilityUpdate {

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 访问限制
     */
    private Visibility visibility;
}
