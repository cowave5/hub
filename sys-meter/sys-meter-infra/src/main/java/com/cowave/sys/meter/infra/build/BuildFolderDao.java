package com.cowave.sys.meter.infra.build;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.constants.Visibility;
import com.cowave.sys.meter.infra.build.mapper.BuildFolderMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.cowave.sys.meter.domain.constants.Visibility.PUBLIC;

/**
 * @author shanhuiming
 */
@Repository
public class BuildFolderDao extends ServiceImpl<BuildFolderMapper, BuildFolder> {

    /**
     * 列表
     */
    public List<BuildFolder> listTree(Collection<Integer> folderIdList) {
        return lambdaQuery().and(wrapper -> {
                    if (CollectionUtils.isNotEmpty(folderIdList)) {
                        wrapper.in(BuildFolder::getFolderId, folderIdList).or();
                    }
                    wrapper.eq(BuildFolder::getVisibility, PUBLIC);
                })
                .orderByAsc(BuildFolder::getParentId)
                .orderByAsc(BuildFolder::getFolderOrder)
                .list();
    }

    /**
     * 修改名称
     */
    public void updateNameById(Integer folderId, String folderName) {
        lambdaUpdate()
                .eq(BuildFolder::getFolderId, folderId)
                .set(BuildFolder::getFolderName, folderName)
                .update();
    }

    /**
     * 修改访问限制
     */
    public void updateVisibilityById(List<Integer> folderIdList, Visibility visibility) {
        lambdaUpdate()
                .in(BuildFolder::getFolderId, folderIdList)
                .set(BuildFolder::getVisibility, visibility)
                .update();
    }

    /**
     * 修改创建人
     */
    public void updateCreatorById(Integer folderId, String userCode, String userName) {
        lambdaUpdate()
                .eq(BuildFolder::getFolderId, folderId)
                .set(BuildFolder::getOwnerCode, userCode)
                .set(BuildFolder::getOwnerName, userName)
                .update();
    }

    /**
     * 修改创建人
     */
    public void transferCreatorById(List<Integer> folderIdList, String preUserCode, String userCode, String userName) {
        lambdaUpdate()
                .in(BuildFolder::getFolderId, folderIdList)
                .eq(BuildFolder::getOwnerCode, preUserCode)
                .set(BuildFolder::getOwnerCode, userCode)
                .set(BuildFolder::getOwnerName, userName)
                .update();
    }
}
