package com.cowave.hub.admin.infra.auth.dao;

import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.configuration.ApplicationProperties;
import com.cowave.hub.admin.domain.rabc.HubDept;
import com.cowave.hub.admin.domain.rabc.HubTenant;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubDeptDtoMapper;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubMenuDtoMapper;
import com.cowave.hub.admin.infra.rabc.mapper.dto.HubRoleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cowave.commons.framework.access.security.Permission.PERMIT_ADMIN;
import static com.cowave.commons.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class UserDetailsDao {
    private final ApplicationProperties applicationProperties;
    private final HubDeptDtoMapper hubDeptDtoMapper;
    private final HubRoleDtoMapper hubRoleDtoMapper;
    private final HubMenuDtoMapper hubMenuDtoMapper;

    public AccessUserDetails newUserDetails(String authType, HubTenant hubTenant, HubUser hubUser) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAuthType(authType);
        // 用户信息
        userDetails.setUserType(hubUser.getUserType().getVal());
        userDetails.setTenantId(hubUser.getTenantId());
        userDetails.setUserId(hubUser.getUserId());
        userDetails.setUserCode(hubUser.getUserCode());
        userDetails.setUsername(hubUser.getUserAccount());
        userDetails.setUserNick(hubUser.getUserName());
        userDetails.setUserPasswd(hubUser.getUserPasswd());
        // 部门信息
        HubDept userDept = hubDeptDtoMapper.getPrimaryDeptByUserId(hubUser.getUserId());
        if (userDept != null) {
            userDetails.setDeptId(userDept.getDeptId());
            userDetails.setDeptCode(userDept.getDeptCode());
            userDetails.setDeptName(userDept.getDeptName());
        }
        // 角色信息
        List<String> roleCodes = hubRoleDtoMapper.getRoleCodesByUserId(hubUser.getUserId());
        userDetails.setRoles(roleCodes);
        // 权限信息
        if(roleCodes.contains(ROLE_ADMIN)){
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
        }else{
            List<String> permitCodes = hubMenuDtoMapper.listPermitsByUserId(hubUser.getTenantId(), hubUser.getUserId());
            userDetails.setPermissions(permitCodes);
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户首页
        userDetails.setTenantIndex(hubTenant.getViewIndex());
        return userDetails;
    }
}
