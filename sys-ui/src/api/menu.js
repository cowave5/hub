import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/admin/api/v1/auth/routes',
    method: 'get'
  })
}
