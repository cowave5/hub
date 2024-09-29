/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc.enums;

import com.cowave.commons.tools.EnumVal;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public enum AccessType implements EnumVal<String> {

    /**
     * 用户令牌
     */
    API("api"),

    /**
     * OAuth授权
     */
    OAUTH("oauth"),

    /**
     * 系统用户
     */
    SYS("sys"),

    /**
     * Ldap用户
     */
    LDAP("ldap"),

    /**
     * Gitlab用户
     */
    GITLAB("gitlab");

    private final String val;

    @Override
    public String val() {
        return val;
    }

    public String generateCode() {
        return val + "-" + java.util.UUID.randomUUID();
    }

    public boolean isTypeEquals(String userCode){
        return userCode.startsWith(val);
    }
}
