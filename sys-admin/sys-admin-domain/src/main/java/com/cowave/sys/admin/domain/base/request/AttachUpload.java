/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachUpload {

    /**
     * 是否公开
     */
    private int isPublic;

    /**
     * 宿主id
     */
    private Long masterId;

    /**
     * 附件分组
     */
    private String attachGroup;

    /**
     * 附件类型
     */
    private String attachType;
}
