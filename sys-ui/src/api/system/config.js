import request from '@/utils/request'

// 查询参数列表
export function listConfig(query) {
  return request({
    url: '/admin/api/v1/config/list',
    method: 'post',
    data: query
  })
}

// 查询参数详细
export function getConfig(configId) {
  return request({
    url: '/admin/api/v1/config/info/' + configId,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data) {
  return request({
    url: '/admin/api/v1/config/add',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateConfig(data) {
  return request({
    url: '/admin/api/v1/config/edit',
    method: 'post',
    data: data
  })
}

// 删除参数配置
export function delConfig(configId) {
  return request({
    url: '/admin/api/v1/config/delete?configId=' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigKey(configKey) {
  return request({
    url: '/admin/api/v1/config/value/' + configKey,
    method: 'get'
  })
}

// 刷新参数缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/config/refresh',
    method: 'get'
  })
}
