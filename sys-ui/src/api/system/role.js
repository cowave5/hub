import request from '@/utils/request'

// 查询角色列表
export function listRole(data) {
  return request({
    url: '/admin/api/v1/role/list',
    method: 'post',
    data: data
  })
}

// 查询角色详细
export function getRole(roleId) {
  return request({
    url: '/admin/api/v1/role/info/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/admin/api/v1/role/add',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/admin/api/v1/role/edit',
    method: 'post',
    data: data
  })
}

// 删除角色
export function delRole(roleId) {
  return request({
    url: '/admin/api/v1/role/delete?roleId=' + roleId,
    method: 'get'
  })
}

// 修改角色菜单
export function changeRoleMenus(data) {
  return request({
    url: '/admin/api/v1/role/change/menus',
    method: 'post',
    data: data
  })
}

// 角色只读修改
export function changeReadonly(roleId, readOnly, roleName) {
  const data = {
    roleId,
    readOnly,
    roleName
  }
  return request({
    url: '/admin/api/v1/role/change/readonly',
    method: 'post',
    data: data
  })
}

// 已授权用户列表
export function allocatedUserList(data) {
  return request({
    url: '/admin/api/v1/role/user/authed',
    method: 'post',
    data: data
  })
}

// 未授权用户列表
export function unallocatedUserList(data) {
  return request({
    url: '/admin/api/v1/role/user/unauthed',
    method: 'post',
    data: data
  })
}

// 取消用户授权角色
export function authUserCancel(data) {
  return request({
    url: '/admin/api/v1/role/user/cancel',
    method: 'post',
    data: data
  })
}

// 授权用户选择
export function authUserSelectAll(data) {
  return request({
    url: '/admin/api/v1/role/user/grant',
    method: 'post',
    data: data
  })
}

// 角色数据权限
export function dataScope(data) {
  return request({
    url: '/system/role/dataScope',
    method: 'put',
    data: data
  })
}
