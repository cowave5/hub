import request from '@/utils/request'

/**
 * 用户组织架构
 */
export function getUserDiagram() {
  return request({
    url: '/admin/api/v1/user/diagram',
    method: 'get'
  })
}

/**
 * 用户列表
 */
export function getUserList(params) {
  return request({
    url: '/admin/api/v1/user',
    method: 'get',
    params: params
  })
}

/**
 * 用户详情
 */
export function getUserInfo(userId) {
  return request({
    url: '/admin/api/v1/user/' + userId,
    method: 'get'
  })
}

/**
 * 新增用户
 */
export function addUser(data) {
  return request({
    url: '/admin/api/v1/user',
    method: 'post',
    data: data
  })
}

/**
 * 删除用户
 */
export function delUser(userIds) {
  return request({
    url: '/admin/api/v1/user/' + userIds,
    method: 'delete'
  })
}

/**
 * 修改用户
 */
export function updateUser(data) {
  return request({
    url: '/admin/api/v1/user',
    method: 'patch',
    data: data
  })
}

/**
 * 修改角色
 */
export function updateUserRoles(userId, userName, roleIds) {
  const data = {
    userId,
    userName,
    roleIds
  }
  return request({
    url: '/admin/api/v1/user/roles',
    method: 'patch',
    data: data
  })
}

/**
 * 修改状态
 */
export function updateUserStatus(userId, userStatus, userName) {
  const data = {
    userId,
    userName,
    userStatus
  }
  return request({
    url: '/admin/api/v1/user/status',
    method: 'patch',
    data: data
  })
}

/**
 * 修改密码
 */
export function updateUserPasswd(userId, userPasswd, userName) {
  const data = {
    userId,
    userPasswd,
    userName
  }
  return request({
    url: '/admin/api/v1/user/passwd',
    method: 'patch',
    data: data
  })
}

/**
 * 用户流程候选人
 */
export function getUserCandidates(userId) {
  let url;
  if (userId === undefined) {
    url = '/admin/api/v1/user/candidates';
  } else {
    url = '/admin/api/v1/user/candidates?userId=' + userId;
  }
  return request({
    url: url,
    method: 'get'
  })
}

/**
 * 查询用户名称
 */
export function getUserNames(userIds) {
  return request({
    url: '/admin/api/v1/user/name/' + userIds,
    method: 'get'
  })
}

/**
 * 查询用户选项
 */
export function getUserMemberOptions(data) {
  return request({
    url: '/admin/api/v1/user/options',
    method: 'post',
    data: data
  })
}
