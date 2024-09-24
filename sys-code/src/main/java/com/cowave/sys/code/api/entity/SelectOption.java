/*
 * Copyright (c) 2017～2099 Cowave All Rights Reserved.
 *
 * For licensing information, please contact: https://www.cowave.com.
 *
 * This code is proprietary and confidential.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.cowave.sys.code.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author shanhuiming
 *
 */
@NoArgsConstructor
@Data
public class SelectOption {

    private Long key;

    private String label;

    private String labelEn;

    private List<SelectOption> children;

    public SelectOption(Long key, String label){
        this.key = key;
        this.label = label;
    }
}
