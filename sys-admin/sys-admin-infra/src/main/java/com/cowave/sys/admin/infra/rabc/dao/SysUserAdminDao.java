package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.auth.LdapUser;
import com.cowave.sys.admin.domain.rabc.SysUserAdmin;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysUserAdminMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserAdminDao extends ServiceImpl<SysUserAdminMapper, SysUserAdmin> {

    /**
     * 账号查询
     */
    public SysUserAdmin getByUserAccount(String userAccount) {
        return lambdaQuery().eq(SysUserAdmin::getUserAccount, userAccount).one();
    }

    public SysUserAdmin getByUserCode(String userCode) {
        return lambdaQuery().eq(SysUserAdmin::getUserCode, userCode).one();
    }

    public void updatePasswdByUserCode(String userCode, String userPasswd) {
        lambdaUpdate().eq(SysUserAdmin::getUserCode, userCode).set(SysUserAdmin::getUserPasswd, userPasswd).update();
    }

    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }
        List<SysUserAdmin> list = lambdaQuery()
                .in(SysUserAdmin::getUserCode, userCodes)
                .select(SysUserAdmin::getUserCode, SysUserAdmin::getUserName)
                .list();
        return Collections.copyToMap(list, SysUserAdmin::getUserCode, SysUserAdmin::getUserName);
    }
}
