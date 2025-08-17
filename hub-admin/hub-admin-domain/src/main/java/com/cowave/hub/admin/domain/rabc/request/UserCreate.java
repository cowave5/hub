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
import com.cowave.hub.admin.domain.rabc.HubUser;
import com.cowave.hub.admin.domain.rabc.HubUserDept;
import com.cowave.hub.admin.domain.rabc.HubUserRole;
import com.cowave.hub.admin.domain.rabc.HubUserDiagram;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreate extends HubUser implements AccessInfoSetter {

    /**
	 * 角色id列表
	 */
	private List<Integer> roleIds;

    /**
	 * 上级用户id列表
	 */
	private List<Integer> parentIds;

    /**
	 * 部门-岗位id列表
	 */
	private List<String> deptPostIds;

    public List<HubUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, roleId -> new HubUserRole(getUserId(), roleId));
    }

    public List<HubUserDiagram> getUserParents(){
        if(CollectionUtils.isNotEmpty(parentIds)){
            return Collections.copyToList(parentIds, parentId -> new HubUserDiagram(getUserId(), parentId, Access.tenantId()));
        }else{
            return List.of(new HubUserDiagram(getUserId(), 0, Access.tenantId()));
        }
    }

    public List<HubUserDept> getUserDeptPosts(){
        return Collections.copyToList(deptPostIds, v -> {
				String[] arr = v.split("-");
				return new HubUserDept(getUserId(), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			});
    }
}
