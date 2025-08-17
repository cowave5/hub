package com.cowave.sys.meter.service.build.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.meter.domain.build.BuildFolder;
import com.cowave.sys.meter.domain.build.BuildFolderMember;
import com.cowave.sys.meter.domain.build.bo.FolderOwner;
import com.cowave.sys.meter.domain.build.request.*;
import com.cowave.sys.meter.domain.constants.FolderRole;
import com.cowave.sys.meter.infra.build.BuildFolderDao;
import com.cowave.sys.meter.infra.build.BuildFolderMemberDao;
import com.cowave.sys.meter.infra.build.mapper.dto.BuildDtoMapper;
import com.cowave.sys.meter.service.build.BuildFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.sys.meter.domain.constants.FolderRole.MANAGER;
import static com.cowave.sys.meter.domain.constants.FolderRole.PARTICIPANT;
import static com.cowave.sys.meter.domain.constants.Visibility.PRIVATE;
import static com.cowave.sys.meter.domain.constants.Visibility.PUBLIC;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BuildFolderServiceImpl implements BuildFolderService {
    public static final TreeNodeConfig DIAGRAM_CONFIG = new TreeNodeConfig()
            .setIdKey("id").setParentIdKey("pid").setNameKey("label").setChildrenKey("children");
    private final BuildDtoMapper buildDtoMapper;
    private final BuildFolderDao buildFolderDao;
    private final BuildFolderMemberDao buildFolderMemberDao;

    @Override
    public List<Tree<Integer>> tree() {
        List<BuildFolderMember> memberList = buildFolderMemberDao.listByUserCode(Access.userCode());
        Map<Integer, FolderRole> folderRoleMap = Collections.copyToMap(
                memberList, BuildFolderMember::getFolderId, BuildFolderMember::getUserRole);
        // public目录以及自己参与的
        List<BuildFolder> folderList = buildFolderDao.listTree(folderRoleMap.keySet());
        return TreeUtil.build(folderList, 0, DIAGRAM_CONFIG, (f, node) -> {
            node.setId(f.getFolderId());
            node.setParentId(f.getParentId());
            node.setName(f.getFolderName());
            node.setWeight(f.getFolderOrder());
            node.put("ownerCode", f.getOwnerCode());
            node.put("visibility", f.getVisibility().getVal());
            FolderRole folderRole = folderRoleMap.get(f.getFolderId());
            if(folderRole != null){
                node.put("folderRole", folderRole.getVal());
            }
        });
    }

    @Override
    public void drag(FolderDrag folderDrag) {
        Integer parentId = folderDrag.getParentId();
        Integer folderId = folderDrag.getFolderId();
        BuildFolder buildFolder = buildFolderDao.getById(folderId);
        // 如果拖拽到另一个private目录下，需要处理下成员合并问题
        if(0 != parentId && !parentId.equals(buildFolder.getParentId())
                && buildFolderDao.getById(parentId).getVisibility() == PRIVATE) {
            List<BuildFolderMember> members;
            if (PRIVATE == buildFolder.getVisibility()) {
                // private目录拖拽到一个private目录下面
                members = buildFolderMemberDao.listByFolderId(List.of(folderId));
            } else {
                // public目录拖拽到一个private目录下面
                members = mergeAndSetChildrenMembers(buildFolder);
            }

            List<BuildFolderMember> parentMembers = buildFolderMemberDao.listByFolderId(List.of(parentId));
            Set<String> distinctUserCode = Collections.copyToSet(parentMembers, BuildFolderMember::getUserCode);
            // 下级目录需要剔除的成员
            for(BuildFolderMember member : members) {
                if(!distinctUserCode.contains(member.getUserCode())) {
                    deleteMember(folderId, member.getUserCode());
                }
            }
        }
        // 设置目录顺序
        List<BuildFolder> folderList = folderDrag.getFolderList();
        buildFolderDao.updateBatchById(folderList);
    }

    @Override
    public void create(BuildFolder buildFolder) {
        Integer parentId = buildFolder.getParentId();
        Integer nextOrder = buildDtoMapper.nextIndexInFolder(parentId);
        buildFolder.setFolderOrder(nextOrder);
        buildFolder.setOwnerCode(Access.userCode());
        buildFolder.setOwnerName(Access.userName());
        buildFolderDao.save(buildFolder);
        if (PRIVATE == buildFolder.getVisibility()) {
            if (parentId == 0 || buildFolderDao.getById(parentId).getVisibility() == PUBLIC) {
                // 将当前以及上级所有目录的创建人设置为owner
                List<FolderOwner> ownerList = buildDtoMapper.folderParentOwners(buildFolder.getFolderId());
                List<BuildFolderMember> members = Collections.copyToList(ownerList,
                        owner -> BuildFolderMember.builder()
                                .folderId(buildFolder.getFolderId())
                                .userCode(owner.getOwnerCode())
                                .userName(owner.getOwnerName())
                                .userRole(MANAGER)
                                .createBy(Access.userCode())
                                .createTime(Access.accessTime())
                                .build());
                buildFolderMemberDao.saveBatch(members);
            } else {
                // 直接继承上一级目录的成员，将自己设置为owner
                List<BuildFolderMember> members = buildFolderMemberDao.listByFolderId(List.of(parentId));
                for (BuildFolderMember folderMember : members) {
                    folderMember.setFolderId(buildFolder.getFolderId());
                    folderMember.setCreateTime(Access.accessTime());
                    if (folderMember.getUserCode().equals(Access.userCode())) {
                        folderMember.setUserRole(MANAGER);
                    }
                }
                buildFolderMemberDao.saveBatch(members);
            }
        }
    }

    @Override
    public void delete(Integer folderId) {
        List<Integer> folderIdList = buildDtoMapper.folderChildrenIds(folderId);
        buildFolderDao.removeBatchByIds(folderIdList);
        buildFolderMemberDao.removeByFolderIds(folderIdList);
    }

    @Override
    public void rename(FolderRename folderRename) {
        buildFolderDao.updateNameById(folderRename.getFolderId(), folderRename.getFolderName());
    }

    @Override
    public void updateVisibility(VisibilityUpdate visibilityUpdate) {
        Integer folderId = visibilityUpdate.getFolderId();
        BuildFolder buildFolder = buildFolderDao.getById(folderId);
        if(buildFolder.getVisibility() == visibilityUpdate.getVisibility()){
            return;
        }

        if(PRIVATE == buildFolder.getVisibility()){
            // private -> public
            buildFolderMemberDao.removeByFolderIds(List.of(folderId));
            buildFolderDao.updateVisibilityById(List.of(folderId), PUBLIC);
        }else{
            // public -> private
            mergeAndSetChildrenMembers(buildFolder);
        }
    }

    @Override
    public Page<BuildFolderMember> listFolderMembers(FolderMemberQuery query) {
        List<BuildFolderMember> existMembers = buildFolderMemberDao.listByFolderId(List.of(query.getFolderId()));
        List<String> excludeUserCodes = Collections.copyToList(existMembers, BuildFolderMember::getUserCode);
        return buildFolderMemberDao.pageFolderMembers(query.getParentId(), query.getUserName(), excludeUserCodes);
    }

    private List<BuildFolderMember> mergeAndSetChildrenMembers(BuildFolder buildFolder) {
        Integer folderId = buildFolder.getFolderId();
        // 所有下级目录的owner
        List<FolderOwner> childrenOwners = buildDtoMapper.folderChildrenOwners(folderId);
        // 所有下级目录的member
        List<Integer> childIdList = buildDtoMapper.folderChildrenIds(folderId);
        List<BuildFolderMember> childrenMembers = buildFolderMemberDao.listByFolderId(childIdList);
        // 合并的成员
        Set<String> distinctUserCode = new HashSet<>();
        List<BuildFolderMember> mergeMembers = new ArrayList<>();
        for(BuildFolderMember member : childrenMembers) {
            if(!distinctUserCode.contains(member.getUserCode())){
                distinctUserCode.add(member.getUserCode());
                mergeMembers.add(member);
            }
        }

        for(FolderOwner owner : childrenOwners) {
            if(!distinctUserCode.contains(owner.getOwnerCode())){
                distinctUserCode.add(owner.getOwnerCode());
                BuildFolderMember member = BuildFolderMember.builder()
                        .userCode(owner.getOwnerCode())
                        .userName(owner.getOwnerName())
                        .userRole(PARTICIPANT)
                        .createBy(Access.userCode())
                        .createTime(Access.accessTime())
                        .build();
                mergeMembers.add(member);
            }
        }

        // 设置下级目录成员
        List<BuildFolder> childrenFolders = buildFolderDao.listByIds(childIdList);
        for(BuildFolder folder : childrenFolders){
            if(PUBLIC == folder.getVisibility()){
                for(BuildFolderMember member : mergeMembers) {
                    member.setFolderId(folder.getFolderId());
                }
                buildFolderMemberDao.saveBatch(mergeMembers);
            }
        }

        // 设置owner为管理员
        buildDtoMapper.syncChildrenOwner(childIdList);

        // 设置下级目录为private
        buildFolderDao.updateVisibilityById(childIdList, PRIVATE);
        return mergeMembers;
    }

    @Override
    public List<BuildFolderMember> listFolderMembers(Integer folderId) {
        return buildFolderMemberDao.listByFolderId(List.of(folderId));
    }

    @Override
    public void addMembers(FolderMemberRequest memberRequest) {
        List<BuildFolderMember> memberList = Collections.copyToList(memberRequest.getMembers(),
                m -> BuildFolderMember.builder()
                        .folderId(memberRequest.getFolderId())
                        .userCode(m.getUserCode())
                        .userName(m.getUserName())
                        .userRole(m.getUserRole())
                        .createBy(Access.userCode())
                        .createTime(Access.accessTime())
                        .build());
        buildFolderMemberDao.saveBatch(memberList);
    }

    @Override
    public void updateMemberRole(FolderMemberUpdate update) {
        buildFolderMemberDao.updateMemberRole(List.of(update.getFolderId()), update.getUserCode(), update.getUserRole());
    }

    @Override
    public void transferFolderMember(FolderMemberTransfer transfer) {
        buildFolderDao.updateCreatorById(transfer.getFolderId(), transfer.getUserCode(), transfer.getUserName());
        buildFolderMemberDao.updateMemberRole(List.of(transfer.getFolderId()), transfer.getUserCode(), MANAGER);
    }

    @Override
    public void deleteMember(Integer folderId, String userCode) {
        BuildFolder folder = buildFolderDao.getById(folderId);
        // 删除人如果创建了下级目录，则转让给当前目录拥有人，并设为管理员
        List<Integer> folderIdList = buildDtoMapper.folderChildrenIds(folderId);
        buildFolderDao.transferCreatorById(folderIdList, userCode, folder.getOwnerCode(), folder.getOwnerName());
        buildFolderMemberDao.updateMemberRole(folderIdList, folder.getOwnerCode(), MANAGER);
        // 删除成员
        buildFolderMemberDao.removeByFolderIds(folderIdList, userCode);
    }
}
