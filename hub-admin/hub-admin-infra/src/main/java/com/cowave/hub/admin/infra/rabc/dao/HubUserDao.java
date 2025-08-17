/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rabc.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.auth.request.ProfileUpdate;
import com.cowave.hub.admin.domain.rabc.enums.EnableStatus;
import com.cowave.hub.admin.domain.rabc.enums.UserType;
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.domain.rabc.request.UserCreate;
import com.cowave.hub.admin.domain.rabc.request.UserExportQuery;
import com.cowave.hub.admin.domain.rabc.request.UserMemberQuery;
import com.cowave.hub.admin.infra.rabc.mapper.HubUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author shanhuiming
 */
@Repository
public class HubUserDao extends ServiceImpl<HubUserMapper, HubUser> {

    /**
     * 用户列表
     */
    public List<HubUser> list(String tenantId, UserExportQuery query) {
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getUserRank()), HubUser::getUserRank, query.getUserRank())
                .like(StringUtils.isNotBlank(query.getUserName()), HubUser::getUserName, query.getUserName())
                .like(StringUtils.isNotBlank(query.getUserPhone()), HubUser::getUserPhone, query.getUserPhone())
                .list();
    }

    /**
     * 查询用户（用户账号）
     */
    public HubUser getByAccount(String tenantId, UserType userType, String userAccount){
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(HubUser::getUserType, userType)
                .eq(HubUser::getUserAccount, userAccount)
                .one();
    }

    /**
     * 查询用户（用户编码）
     */
    public HubUser getByCode(String userCode){
        return lambdaQuery().eq(HubUser::getUserCode, userCode).one();
    }

    /**
     * 账号统计（排除自己）
     */
    public long countByAccount(String tenantId, UserType userType, String userAccount, Integer userId){
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(HubUser::getUserType, userType)
                .eq(HubUser::getUserAccount, userAccount)
                .ne(userId != null, HubUser::getUserId, userId)
                .count();
    }

    /**
     * 查询姓名（用户id）
     */
    public String queryNameById(Integer userId) {
        return lambdaQuery()
                .eq(HubUser::getUserId, userId)
                .select(HubUser::getUserName)
                .oneOpt().map(HubUser::getUserName).orElse(null);
    }

    /**
     * 查询姓名（用户id列表）
     */
    public List<String> getNamesById(String tenantId, List<Integer> userIds){
        if(userIds.isEmpty()){
            return List.of();
        }
        List<HubUser> list = lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .in(HubUser::getUserId, userIds)
                .select(HubUser::getUserName)
                .list();
        return Collections.copyToList(list, HubUser::getUserName);
    }

    /**
     * 查询姓名（用户编号）
     */
    public String queryNameByCode(String userCode){
        return lambdaQuery()
                .eq(HubUser::getUserCode, userCode)
                .select(HubUser::getUserName)
                .oneOpt().map(HubUser::getUserName).orElse(null);
    }

    /**
     * 修改用户
     */
    public void updateUser(UserCreate user){
        lambdaUpdate().eq(HubUser::getUserId, user.getUserId())
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserName, user.getUserName())
                .set(HubUser::getUserAccount, user.getUserAccount())
                .set(HubUser::getUserSex, user.getUserSex())
                .set(HubUser::getUserPhone, user.getUserPhone())
                .set(HubUser::getUserEmail, user.getUserEmail())
                .set(HubUser::getUserRank, user.getUserRank())
                .set(HubUser::getRemark, user.getRemark())
                .update();
    }

    /**
     * 修改状态
     */
    public void updateStatusById(Integer userId, EnableStatus status){
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserStatus, status)
                .update();
    }

    /**
     * 绑定二次认证
     */
    public void setMfa(Integer userId, String mfaKey) {
        lambdaUpdate().eq(HubUser::getUserId, userId).set(HubUser::getMfa, mfaKey).update();
    }

    /**
     * 解除二次认证
     */
    public void deleteMfa(Integer userId) {
        lambdaUpdate().eq(HubUser::getUserId, userId).set(HubUser::getMfa, null).update();
    }

    /**
     * 修改密码
     */
    public void updatePasswdById(Integer userId, String passwd){
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserPasswd, passwd)
                .update();
    }

    /**
     * 修改个人信息
     */
    public void updateProfileById(Integer userId, ProfileUpdate profile){
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserSex, profile.getUserSex())
                .set(HubUser::getUserName, profile.getUserName())
                .set(HubUser::getUserEmail, profile.getUserEmail())
                .set(HubUser::getUserPhone, profile.getUserPhone())
                .update();
    }

    /**
     * 查询姓名（用户编码列表）
     */
    public Map<String, String> queryCodeNameMap(Collection<String> userCodes){
        if(userCodes.isEmpty()){
            return new HashMap<>();
        }

        List<HubUser> list = lambdaQuery()
                .in(HubUser::getUserCode, userCodes)
                .select(HubUser::getUserCode, HubUser::getUserName)
                .list();
        return Collections.copyToMap(list, HubUser::getUserCode, HubUser::getUserName);
    }

    /**
     * 用户选项列表
     */
    public Page<HubUser> getUserOptions(String tenantId, UserMemberQuery query) {
        return lambdaQuery().eq(HubUser::getTenantId, tenantId)
                .like(StringUtils.isNotBlank(query.getUserName()), HubUser::getUserName, query.getUserName())
                .notIn(CollectionUtils.isNotEmpty(query.getUserCodes()), HubUser::getUserCode, query.getUserCodes())
                .page(Access.page());
    }

    public void updateLdapByCode(HubUser hubUser) {
        lambdaUpdate()
                .eq(HubUser::getUserCode, hubUser.getUserCode())
                .set(HubUser::getUserName, hubUser.getUserName())
                .set(HubUser::getUserPhone, hubUser.getUserPhone())
                .set(HubUser::getUserEmail, hubUser.getUserEmail())
                .update();
    }

    public void updateGitlabByCode(HubUser hubUser) {
        lambdaUpdate()
                .eq(HubUser::getUserCode, hubUser.getUserCode())
                .set(HubUser::getUserName, hubUser.getUserName())
                .set(HubUser::getUserEmail, hubUser.getUserEmail())
                .update();
    }
}
