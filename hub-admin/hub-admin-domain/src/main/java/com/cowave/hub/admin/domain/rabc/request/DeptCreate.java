/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.rabc.request;

import com.cowave.commons.framework.access.Access;
import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.tools.Collections;
import com.cowave.hub.admin.domain.rabc.HubDept;
import com.cowave.hub.admin.domain.rabc.HubDeptDiagram;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptCreate extends HubDept implements AccessInfoSetter {

    /**
	 * 上级部门Id列表
	 */
	@NotEmpty(message = "{admin.dept.parentIds.null}")
	private List<Integer> parentIds;

    public List<HubDeptDiagram> getDeptParents(){
        if(CollectionUtils.isNotEmpty(parentIds)){
            return Collections.copyToList(parentIds, parentId -> new HubDeptDiagram(getDeptId(), parentId, Access.tenantId()));
        }else{
            return List.of(new HubDeptDiagram(getDeptId(), 0, Access.tenantId()));
        }
    }
}
