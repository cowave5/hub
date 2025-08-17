import request from '@/utils/request'

/**
 * 获取个人信息
 */
export function getUserProfile() {
  return request({
    url: '/admin/api/v1/profile',
    method: 'get'
  })
}

/**
 * 修改个人信息
 */
export function updateUserProfile(data) {
  return request({
    url: '/admin/api/v1/profile',
    method: 'patch',
    data: data
  })
}

/**
 * 密码重置
 */
export function resetPasswd(oldPasswd, newPasswd) {
  const data = {
    oldPasswd,
    newPasswd
  }
  return request({
    url: '/admin/api/v1/profile/passwd',
    method: 'patch',
    data: data
  })
}

/**
 * MFA获取
 */
export function getMFA() {
  return request({
    url: '/admin/api/v1/profile/mfa',
    method: 'get'
  })
}

/**
 * MFA绑定
 */
export function enableMFA(data) {
  return request({
    url: '/admin/api/v1/profile/mfa/enable',
    method: 'patch',
    data: data
  })
}

/**
 * MFA解除
 */
export function disableMFA(data) {
  return request({
    url: '/admin/api/v1/profile/mfa/disable',
    method: 'patch',
    data: data
  })
}

/**
 * 获取权限Api
 */
export function getAuthApiTree() {
  return request({
    url: '/admin/api/v1/profile/api/tree',
    method: 'get'
  })
}

/**
 * Api令牌列表
 */
export function listApiToken() {
  return request({
    url: '/admin/api/v1/profile/api/token',
    method: 'get'
  })
}

/**
 * 创建Api令牌
 */
export function creatApiToken(data) {
  return request({
    url: '/admin/api/v1/profile/api/token',
    method: 'post',
    data: data
  })
}

/**
 * 删除Api令牌
 */
export function deleteApiToken(id) {
  return request({
    url: '/admin/api/v1/profile/api/token/' + id,
    method: 'delete'
  })
}
