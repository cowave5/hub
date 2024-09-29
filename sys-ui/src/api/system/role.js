import request from '@/utils/request'

/**
 * 角色列表
 */
export function getRoleList(params) {
  return request({
    url: '/admin/api/v1/role',
    method: 'get',
    params: params
  })
}

/**
 * 角色详情
 */
export function getRoleInfo(roleId) {
  return request({
    url: '/admin/api/v1/role/' + roleId,
    method: 'get'
  })
}

/**
 * 新增角色
 */
export function addRole(data) {
  return request({
    url: '/admin/api/v1/role',
    method: 'post',
    data: data
  })
}

/**
 * 删除角色
 */
export function delRole(roleIds) {
  return request({
    url: '/admin/api/v1/role/' + roleIds,
    method: 'delete'
  })
}

/**
 * 修改角色
 */
export function updateRole(data) {
  return request({
    url: '/admin/api/v1/role',
    method: 'patch',
    data: data
  })
}

/**
 * 修改角色菜单
 */
export function updateRoleMenus(data) {
  return request({
    url: '/admin/api/v1/role/menus',
    method: 'patch',
    data: data
  })
}

/**
 * 授权用户角色
 */
export function grantRoleUser(data) {
  return request({
    url: '/admin/api/v1/role/user/grant',
    method: 'post',
    data: data
  })
}

/**
 * 取消用户角色
 */
export function cancelRoleUser(data) {
  return request({
    url: '/admin/api/v1/role/user/cancel',
    method: 'post',
    data: data
  })
}

/**
 * 用户列表（已授权）
 */
export function getAuthedUser(params) {
  return request({
    url: '/admin/api/v1/role/users/authed',
    method: 'get',
    params: params
  })
}

/**
 * 用户列表（未授权）
 */
export function getUnAuthedUser(params) {
  return request({
    url: '/admin/api/v1/role/users/unAuthed',
    method: 'get',
    params: params
  })
}

/**
 * 查询角色名称
 */
export function getRoleNames(roleIds) {
  return request({
    url: '/admin/api/v1/role/name/' + roleIds,
    method: 'get'
  })
}

