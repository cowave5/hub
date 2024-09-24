/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.admin.api.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class CacheInfo {

    private Properties info;

    private Object dbSize;

    private List<Map<String, String>> commandStats;
}
