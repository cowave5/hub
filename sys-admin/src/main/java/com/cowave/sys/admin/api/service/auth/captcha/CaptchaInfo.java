/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.service.auth.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaptchaInfo {

    private String uuid;

    private String img;

    private boolean captchaOnOff;

    private boolean registerOnOff;

    private String oauthGitlabUrl;

    public CaptchaInfo(boolean registerOnOff, String oauthGitlabUrl){
        this.registerOnOff = registerOnOff;
        this.oauthGitlabUrl = oauthGitlabUrl;
    }
}
