/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.service.auth;

import com.cowave.sys.admin.domain.auth.dto.UserProfile;
import com.cowave.sys.admin.domain.auth.request.PasswdReset;
import com.cowave.sys.admin.domain.auth.request.ProfileUpdate;
import com.cowave.sys.admin.domain.base.request.AttachUpload;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author shanhuiming
 */
public interface ProfileService {

    /**
     * 详情
     */
    UserProfile info() throws Exception;

    /**
     * 修改
     */
    void edit(ProfileUpdate profile);

    /**
     * 重置密码
     */
    void resetPasswd(PasswdReset passwdReset);

    /**
     * 上传头像
     */
    String uploadAvatar(MultipartFile file, AttachUpload attachUpload) throws Exception;
}
