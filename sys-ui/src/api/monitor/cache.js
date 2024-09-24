import request from '@/utils/request'

// 查询缓存详细
export function getCache() {
  return request({
    url: '/admin//api/v1/sys/cache',
    method: 'get'
  })
}
