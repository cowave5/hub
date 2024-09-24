/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service;

import com.cowave.sys.admin.api.entity.UserProfile;

/**
 *
 * @author shanhuiming
 *
 */
public interface ProfileService {

    /**
     * 详情
     */
    UserProfile info();

    /**
     * 修改
     */
    void edit(UserProfile userProfile);

    /**
     * 重置密码
     */
    void resetPasswd(UserProfile userProfile);
}
