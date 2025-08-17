/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.hub.admin.domain.sys.HubDict;
import com.cowave.hub.admin.domain.sys.dto.DictInfoDto;
import com.cowave.hub.admin.infra.sys.mapper.HubDictMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubDictDao extends ServiceImpl<HubDictMapper, HubDict> {

    /**
     * 获取字典
     */
    public HubDict getByCode(String dictCode) {
        return lambdaQuery().eq(HubDict::getDictCode, dictCode).one();
    }

    /**
     * 获取类型字典
     */
    public List<HubDict> listByType(String typeCode) {
        return lambdaQuery().eq(HubDict::getParentCode, typeCode).list();
    }

    /**
     * 更新字典
     */
    public void updateDict(DictInfoDto dict){
        lambdaUpdate().eq(HubDict::getId, dict.getId())
                .set(HubDict::getUpdateBy, Access.userCode())
                .set(HubDict::getUpdateTime, new Date())
                .set(HubDict::getDictCode, dict.getDictCode())
                .set(HubDict::getDictName, dict.getDictName())
                .set(HubDict::getDictValue, dict.getDictValue())
                .set(HubDict::getDictOrder, dict.getDictOrder())
                .set(HubDict::getValueParser, dict.getValueParser())
                .set(HubDict::getValueType, dict.getValueType())
                .set(HubDict::getIsDefault, dict.getIsDefault())
                .set(HubDict::getCss, dict.getCss())
                .set(HubDict::getStatus, dict.getStatus())
                .set(HubDict::getRemark, dict.getRemark())
                .update();
    }

    /**
     * 更新上级字典码
     */
    public void updateParentCode(String newParent, String oldParent){
        lambdaUpdate()
                .eq(HubDict::getParentCode, oldParent)
                .set(HubDict::getParentCode, newParent)
                .update();
    }

    /**
     * 删除类型字典
     */
    public void removeByType(String parentCode){
        lambdaUpdate().eq(HubDict::getParentCode, parentCode).remove();
    }
}
