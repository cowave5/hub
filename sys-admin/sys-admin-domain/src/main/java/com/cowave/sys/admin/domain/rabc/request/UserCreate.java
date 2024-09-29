/*
 * Copyright (c) 2017～2025 Cowave All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.sys.admin.domain.rabc.request;

import com.cowave.commons.framework.access.security.AccessInfoSetter;
import com.cowave.commons.tools.Collections;
import com.cowave.sys.admin.domain.rabc.SysUser;
import com.cowave.sys.admin.domain.rabc.SysUserDept;
import com.cowave.sys.admin.domain.rabc.SysUserRole;
import com.cowave.sys.admin.domain.rabc.SysUserTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanhuiming
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreate extends SysUser implements AccessInfoSetter {

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

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, v -> new SysUserRole(getUserId(), v));
    }

    public List<SysUserTree> getUserParents(){
        return Collections.copyToList(parentIds, v -> new SysUserTree(getUserId(), v));
    }

    public List<SysUserDept> getUserDeptPosts(){
        return Collections.copyToList(deptPostIds, v -> {
				String[] arr = v.split("-");
				return new SysUserDept(getUserId(), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			});
    }
}
