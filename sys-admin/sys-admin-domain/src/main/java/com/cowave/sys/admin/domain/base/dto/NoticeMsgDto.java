/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class NoticeMsgDto {

    /**
     * 已读状态
     */
    private Integer readStatus;

    /**
     * 已读反馈
     */
    private String readBack;

    /**
     * 公告id
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告状态
     */
    private Integer noticeStatus;

    /**
     * 公告类型
     */
    private Integer noticeType;

    /**
     * 公告等级
     */
    private Integer noticeLevel;

    /**
     * 是否系统公告
     */
    private Integer isSystem;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    /**
     * 创建人
     */
    private String createBy;
}
