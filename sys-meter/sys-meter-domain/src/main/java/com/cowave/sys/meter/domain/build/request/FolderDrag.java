package com.cowave.sys.meter.domain.build.request;

import com.cowave.commons.tools.Collections;
import com.cowave.sys.meter.domain.build.BuildFolder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shanhuiming
 */
@Data
public class FolderDrag {

    /**
     * 拽入目录的id
     */
    private Integer parentId;

    /**
     * 被拽目录的id
     */
    private Integer folderId;

    /**
     * 拽入目录的子目录id
     */
    private List<Integer> childrenIds;

    public List<BuildFolder> getFolderList() {
        AtomicInteger order = new AtomicInteger(0);
        return Collections.copyToList(childrenIds, id ->
                BuildFolder.builder().folderId(id).parentId(parentId).folderOrder(order.incrementAndGet()).build());
    }
}
