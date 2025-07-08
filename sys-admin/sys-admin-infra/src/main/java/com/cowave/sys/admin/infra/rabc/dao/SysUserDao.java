/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.auth.request.ProfileUpdate;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.request.UserCreate;
import com.cowave.sys.admin.domain.rabc.request.UserExportQuery;
import com.cowave.sys.admin.infra.rabc.dao.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class SysUserDao extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 用户列表
     */
    public List<SysUser> list(String tenantId, UserExportQuery query) {
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getUserRank()), SysUser::getUserRank, query.getUserRank())
                .like(StringUtils.isNotBlank(query.getUserName()), SysUser::getUserName, query.getUserName())
                .like(StringUtils.isNotBlank(query.getUserPhone()), SysUser::getUserPhone, query.getUserPhone())
                .list();
    }

    /**
     * 查询用户（用户账号）
     */
    public SysUser getByAccount(String tenantId, String userAccount){
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(SysUser::getUserAccount, userAccount)
                .one();
    }

    /**
     * 查询用户（用户编码）
     */
    public SysUser getByCode(String userCode){
        return lambdaQuery().eq(SysUser::getUserCode, userCode).one();
    }

    /**
     * 账号统计（排除自己）
     */
    public long countByAccount(String tenantId, String userAccount, Integer userId){
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(SysUser::getUserAccount, userAccount)
                .ne(userId != null, SysUser::getUserId, userId)
                .count();
    }

    /**
     * 查询姓名（用户id）
     */
    public String queryNameById(Integer userId) {
        return lambdaQuery()
                .eq(SysUser::getUserId, userId)
                .select(SysUser::getUserName)
                .oneOpt().map(SysUser::getUserName).orElse(null);
    }

    /**
     * 查询姓名（用户id列表）
     */
    public List<String> getNamesById(String tenantId, List<Integer> userIds){
        if(userIds.isEmpty()){
            return List.of();
        }
        List<SysUser> list = lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .in(SysUser::getUserId, userIds)
                .select(SysUser::getUserName)
                .list();
        return Collections.copyToList(list, SysUser::getUserName);
    }

    /**
     * 查询姓名（用户编号）
     */
    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(SysUser::getUserCode, userCode)
                .select(SysUser::getUserName)
                .oneOpt().map(SysUser::getUserName).orElse(null);
    }

    /**
     * 修改用户
     */
    public void updateUser(UserCreate user){
        lambdaUpdate().eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserName, user.getUserName())
                .set(SysUser::getUserAccount, user.getUserAccount())
                .set(SysUser::getUserSex, user.getUserSex())
                .set(SysUser::getUserPhone, user.getUserPhone())
                .set(SysUser::getUserEmail, user.getUserEmail())
                .set(SysUser::getUserRank, user.getUserRank())
                .set(SysUser::getRemark, user.getRemark())
                .update();
    }

    /**
     * 修改状态
     */
    public void updateStatusById(Integer userId, Integer status){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserStatus, status)
                .update();
    }

    /**
     * 绑定二次认证
     */
    public void setMfa(Integer userId, String mfaKey) {
        lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getMfa, mfaKey).update();
    }

    /**
     * 解除二次认证
     */
    public void deleteMfa(Integer userId) {
        lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getMfa, null).update();
    }

    /**
     * 修改密码
     */
    public void updatePasswdById(Integer userId, String passwd){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserPasswd, passwd)
                .update();
    }

    /**
     * 修改个人信息
     */
    public void updateProfileById(Integer userId, ProfileUpdate profile){
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserSex, profile.getUserSex())
                .set(SysUser::getUserName, profile.getUserName())
                .set(SysUser::getUserEmail, profile.getUserEmail())
                .set(SysUser::getUserPhone, profile.getUserPhone())
                .update();
    }

    public Map<String, String> queryCodeNameMap(List<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<SysUser> list = lambdaQuery()
                .in(SysUser::getUserCode, userCodes)
                .select(SysUser::getUserCode, SysUser::getUserName)
                .list();
        return Collections.copyToMap(list, SysUser::getUserCode, SysUser::getUserName);
    }
}
