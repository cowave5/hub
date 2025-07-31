package com.cowave.sys.meter.infra.build;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.meter.domain.build.BuildFolderMember;
import com.cowave.sys.meter.domain.constants.FolderRole;
import com.cowave.sys.meter.infra.build.mapper.BuildFolderMemberMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class BuildFolderMemberDao extends ServiceImpl<BuildFolderMemberMapper, BuildFolderMember> {

    /**
     * 获取目录成员
     */
    public Page<BuildFolderMember> pageFolderMembers(Integer folderId, String userName, List<String> excludeUserCodes) {
        return lambdaQuery()
                .eq(BuildFolderMember::getFolderId, folderId)
                .like(StringUtils.isNotBlank(userName), BuildFolderMember::getUserName, userName)
                .notIn(CollectionUtils.isNotEmpty(excludeUserCodes), BuildFolderMember::getUserCode, excludeUserCodes)
                .page(Access.page());
    }

    /**
     * 获取目录成员
     */
    public List<BuildFolderMember> listByFolderId(List<Integer> folderIdList) {
        return lambdaQuery().in(BuildFolderMember::getFolderId, folderIdList).list();
    }

    /**
     * 获取人员目录
     */
    public List<BuildFolderMember> listByUserCode(String userCode) {
        return lambdaQuery().eq(BuildFolderMember::getUserCode, userCode).list();
    }

    /**
     * 删除目录成员
     */
    public void removeByFolderIds(List<Integer> folderIdList) {
        lambdaUpdate().in(BuildFolderMember::getFolderId, folderIdList).remove();
    }

    /**
     * 删除目录成员
     */
    public void removeByFolderIds(List<Integer> folderIdList, String userCode) {
        lambdaUpdate()
                .in(BuildFolderMember::getFolderId, folderIdList)
                .eq(BuildFolderMember::getUserCode, userCode)
                .remove();
    }

    /**
     * 修改成员角色
     */
    public void updateMemberRole(List<Integer> folderIdList, String userCode, FolderRole folderRole){
        lambdaUpdate()
                .in(BuildFolderMember::getFolderId, folderIdList)
                .eq(BuildFolderMember::getUserCode, userCode)
                .set(BuildFolderMember::getUserRole, folderRole)
                .update();
    }
}
