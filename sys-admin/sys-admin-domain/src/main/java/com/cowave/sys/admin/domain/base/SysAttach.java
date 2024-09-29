/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Data
public class SysAttach {

    /**
     * 附件id
     */
    @TableId(type = IdType.AUTO)
    private Long attachId;

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

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件大小
     */
    private Long attachSize;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 附件状态
     */
    private Integer attachStatus = 1;

    /**
     * 是否公开的
     */
    private Integer isPublic = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否设置过期
     */
    private Integer expireSet = 0;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 预览地址
     */
    @TableField(exist = false)
    private String viewUrl;
}
