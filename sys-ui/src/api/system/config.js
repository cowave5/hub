import request from '@/utils/request'

/**
 * 配置列表
 */
export function getConfigList(params) {
  return request({
    url: '/admin/api/v1/config',
    method: 'get',
    params: params
  })
}

/**
 * 配置详情
 */
export function getConfigInfo(configId) {
  return request({
    url: '/admin/api/v1/config/' + configId,
    method: 'get'
  })
}

/**
 * 新增配置
 */
export function addConfig(data) {
  return request({
    url: '/admin/api/v1/config',
    method: 'post',
    data: data
  })
}

/**
 * 删除配置
 */
export function delConfig(configIds) {
  return request({
    url: '/admin/api/v1/config/' + configIds,
    method: 'delete'
  })
}

/**
 * 修改配置
 */
export function updateConfig(data) {
  return request({
    url: '/admin/api/v1/config',
    method: 'patch',
    data: data
  })
}

/**
 * 刷新配置
 */
export function refreshConfig() {
  return request({
    url: '/admin/api/v1/config/reset',
    method: 'get'
  })
}

/**
 * 获取配置值
 */
export function getConfigValue(configKey) {
  return request({
    url: '/admin/api/v1/config/value/' + configKey,
    method: 'get'
  })
}


