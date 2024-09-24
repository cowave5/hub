/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.blog.api.service;

import com.cowave.sys.blog.api.entity.NoteInfo;
import org.springframework.feign.codec.Response;
import org.springframework.ui.ModelMap;

/**
 *
 * @author shanhuiming
 *
 */
public interface NoteService {

    Response.Page<NoteInfo> notePage(ModelMap modelMap);
}
