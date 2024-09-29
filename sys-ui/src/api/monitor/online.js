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
 * 强退用户
 */
export function forceLogout(accessId) {
  return request({
    url: '/admin/api/v1/auth/outline/' + accessId,
    method: 'get'
  })
}
