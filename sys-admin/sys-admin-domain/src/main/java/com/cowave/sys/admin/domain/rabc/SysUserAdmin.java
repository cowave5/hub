package com.cowave.sys.admin.domain.rabc;

import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.commons.framework.access.security.AccessUserDetails;
import com.cowave.commons.framework.access.security.Permission;
import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import lombok.Data;

import java.util.List;

import static com.cowave.sys.admin.domain.auth.AccessType.ADMIN;

/**
 * @author shanhuiming
 */
@Data
public class SysUserAdmin {

    /**
     * 用户id
     */
    @TableId
    private Integer userId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
	 * 用户密码
	 */
	private String userPasswd;

    public AccessUserDetails newUserDetails() {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setType(ADMIN.val());
        userDetails.setUserId(userId);
        userDetails.setUserCode(userCode);
        userDetails.setUsername(userAccount);
        userDetails.setUserNick(userName);
        userDetails.setUserPasswd(userPasswd);
        userDetails.setRoles(List.of(Permission.ROLE_ADMIN));
        userDetails.setPermissions(List.of(Permission.PERMIT_ADMIN));
        return userDetails;
    }

    public UserProfile newUserProfile(){
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setUserCode(userCode);
        userProfile.setUserType(userType);
        userProfile.setUserName(userName);
        userProfile.setUserAccount(userAccount);
        return userProfile;
    }
}
