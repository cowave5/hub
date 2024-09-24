/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.mapper;

import com.cowave.sys.admin.api.entity.UserProfile;
import com.cowave.sys.admin.api.entity.UserRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author shanhuiming
 *
 */
@Mapper
public interface ProfileMapper {

    /**
     * 注册
     */
    void register(UserRegister userRegister);

    /**
     * 初始角色
     */
    void initRole(Long userId);

    /**
     * 历史通知
     */
    void initHistoryNotice(Long userId);

    /**
     * 统计通知
     */
    void updateNoticeStat();

    /**
     * 详情
     */
    UserProfile oauthInfo(Long userId);

    /**
     * 详情
     */
    UserProfile info(Long userId);

    /**
     * 修改
     */
    void edit(UserProfile userProfile);

    /**
     * 密码
     */
    String queryPasswd(Long userId);

    /**
     * 重置密码
     */
    void resetPasswd(@Param("userId") Long userId, @Param("passwd") String passwd);
}
