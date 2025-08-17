package com.cowave.sys.meter.infra.build.mapper.dto;

import com.cowave.sys.meter.domain.build.bo.FolderOwner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface BuildDtoMapper {

    /**
     * 目录中下一个序列
     */
    Integer nextIndexInFolder(Integer parentId);

    /**
     * 下一个构建序列
     */
    Long nextIndexOfBuild(Integer buildId);

    /**
     * 下级目录id
     */
    List<Integer> folderChildrenIds(Integer folderId);

    /**
     * 下级目录拥有人
     */
    List<FolderOwner> folderChildrenOwners(Integer folderId);

    /**
     * 上级目录拥有人
     */
    List<FolderOwner> folderParentOwners(Integer folderId);

    /**
     * 设置下面目录管理员
     */
    void syncChildrenOwner(List<Integer> list);
}
