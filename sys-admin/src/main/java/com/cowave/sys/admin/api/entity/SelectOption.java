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

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class SelectOption {

    private String key;

    private String label;

    private String labelEn;

    private List<SelectOption> children;
}
