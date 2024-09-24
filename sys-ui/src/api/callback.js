import request from '@/utils/request'

export function gitlab(code) {
  return request({
    url: '/admin/api/v1/oauth/callback/gitlab?code=' + code,
    headers: {
      requireToken: false
    },
    method: 'get'
  })
}
