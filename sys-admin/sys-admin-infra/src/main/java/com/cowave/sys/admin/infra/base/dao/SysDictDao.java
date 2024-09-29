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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.commons.framework.access.Access;
import com.cowave.sys.admin.domain.base.SysDict;
import com.cowave.sys.admin.domain.base.dto.DictInfoDto;
import com.cowave.sys.admin.infra.base.dao.mapper.SysDictMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class SysDictDao extends ServiceImpl<SysDictMapper, SysDict> {

    public void updateDict(DictInfoDto sysDict){
        lambdaUpdate().eq(SysDict::getId, sysDict.getId())
                .set(SysDict::getUpdateBy, Access.userCode())
                .set(SysDict::getUpdateTime, new Date())
                .set(SysDict::getDictCode, sysDict.getDictCode())
                .set(SysDict::getDictName, sysDict.getDictName())
                .set(SysDict::getDictValue, sysDict.getDictValue())
                .set(SysDict::getDictOrder, sysDict.getDictOrder())
                .set(SysDict::getValueParser, sysDict.getValueParser())
                .set(SysDict::getValueType, sysDict.getValueType())
                .set(SysDict::getIsDefault, sysDict.getIsDefault())
                .set(SysDict::getCss, sysDict.getCss())
                .set(SysDict::getStatus, sysDict.getStatus())
                .set(SysDict::getRemark, sysDict.getRemark())
                .update();
    }

    public void updateParentCode(String newParent, String oldParent){
        lambdaUpdate().eq(SysDict::getParentCode, oldParent).set(SysDict::getParentCode, newParent).update();
    }

    public void deleteByParentCode(String parentCode){
        lambdaUpdate().eq(SysDict::getParentCode, parentCode).remove();
    }
}
