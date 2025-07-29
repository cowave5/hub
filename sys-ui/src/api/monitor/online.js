import request from '@/utils/request'

/**
 * 在线用户列表
 */
export function list(data) {
  return request({
    url: '/admin/api/v1/auth/online',
    method: 'post',
    data: data
  })
}

/**
 * 删除令牌
 */
export function revokeAccess(accessType, userAccount, accessId) {
  return request({
    url: '/admin/api/v1/auth/access?type=' + accessType + '&account=' + userAccount + '&id=' + accessId,
    method: 'delete'
  })
}

/**
 * 强制退出
 */
export function revokeRefresh(accessType, userAccount) {
  return request({
    url: '/admin/api/v1/auth/refresh?type=' + accessType + '&account=' + userAccount,
    method: 'delete'
  })
}
