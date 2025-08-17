/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.flow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.flow.Leave;

/**
 * @author shanhuiming
 */
public interface LeaveFlowService {

    /**
     * 列表
     */
    Page<Leave> list(Leave leave);

    /**
     * 详情
     */
    Leave info(Long id);

    /**
     * 新增请假
     */
    void add(Leave leave);

    /**
     * 修改请假
     */
    int edit(Leave leave);

    /**
     * 删除请假
     */
    void delete(Long[] ids);

    /**
     * 撤销
     */
    void revocate(Long id);
}
