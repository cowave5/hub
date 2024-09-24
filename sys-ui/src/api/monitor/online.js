import request from '@/utils/request'

// 查询在线用户列表
export function list(data) {
  return request({
    url: '/admin/api/v1/auth/online',
    method: 'post',
    data: data
  })
}

// 强退用户
export function forceLogout(tokenType, userAccount) {
  return request({
    url: '/admin/api/v1/auth/outline/' + tokenType + '/' + userAccount,
    method: 'get'
  })
}
