/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.infra.base.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.SysConfig;
import com.cowave.sys.admin.domain.base.request.ConfigQuery;
import com.cowave.sys.admin.infra.base.dao.mapper.SysConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysConfigDao extends ServiceImpl<SysConfigMapper, SysConfig> {

    public Page<SysConfig> queryPage(ConfigQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .ge(query.getBeginTime() != null, SysConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, SysConfig::getCreateTime, query.getEndTime())
                .orderByAsc(SysConfig::getConfigId)
                .page(Access.page());
    }

    public List<SysConfig> queryList(ConfigQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .ge(query.getBeginTime() != null, SysConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, SysConfig::getCreateTime, query.getEndTime())
                .orderByAsc(SysConfig::getConfigId)
                .list();
    }

    public void updateConfig(SysConfig sysConfig){
        lambdaUpdate().eq(SysConfig::getConfigId, sysConfig.getConfigId())
                .set(SysConfig::getUpdateBy, Access.userCode())
                .set(SysConfig::getUpdateTime, new Date())
                .set(SysConfig::getConfigName, sysConfig.getConfigName())
                .set(SysConfig::getConfigKey, sysConfig.getConfigKey())
                .set(SysConfig::getValueParser, sysConfig.getValueParser())
                .set(SysConfig::getValueType, sysConfig.getValueType())
                .set(SysConfig::getIsDefault, sysConfig.getIsDefault())
                .set(SysConfig::getRemark, sysConfig.getRemark())
                .update();
    }
}
