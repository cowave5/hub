package com.cowave.sys.meter.service.build;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.build.BuildFolderMember;
import com.cowave.sys.meter.domain.build.request.*;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface BuildFolderService {

    /**
     * 目录结构
     */
    List<Tree<Integer>> tree();

    /**
     * 目录拖拽
     */
    void drag(FolderDrag folderDrag);

    /**
     * 新增
     */
    void create(BuildFolder buildFolder);

    /**
     * 删除
     */
    void delete(Integer folderId);

    /**
     * 修改目录名称
     */
    void rename(FolderRename folderRename);

    /**
     * 修改目录访问限制
     */
    void updateVisibility(VisibilityUpdate visibilityUpdate);

    /**
     * 目录成员选项
     */
    Page<BuildFolderMember> listFolderMembers(FolderMemberQuery query);

    /**
     * 目录成员列表
     */
    List<BuildFolderMember> listFolderMembers(Integer folderId);

    /**
     * 追加目录成员
     */
    void addMembers(FolderMemberRequest memberRequest);

    /**
     * 修改成员角色
     */
    void updateMemberRole(FolderMemberUpdate update);

    /**
     * 转让目录
     */
    void transferFolderMember(FolderMemberTransfer transfer);

    /**
     * 删除目录成员
     */
    void deleteMember(Integer folderId, String userCode);
}
