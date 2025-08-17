import request from '@/utils/request'

/**
 * 获取配置
 */
export function getConfig(serverType) {
  return request({
    url: '/admin/api/v1/oauth/config/' + serverType,
    method: 'get'
  })
}

/**
 * 保存配置
 */
export function saveConfig(data) {
  return request({
    url: '/admin/api/v1/oauth/config',
    method: 'patch',
    data: data
  })
}

/**
 * 用户列表
 */
export function listUser(params) {
  return request({
    url: '/admin/api/v1/oauth/user',
    method: 'get',
    params: params
  })
}

/**
 * 授权客户端列表
 */
export function getClientList(params) {
  return request({
    url: '/admin/api/v1/oauth/client',
    method: 'get',
    params: params
  })
}

/**
 * 新增客户端
 */
export function addClient(data) {
  return request({
    url: '/admin/api/v1/oauth/client',
    method: 'post',
    data: data
  })
}

/**
 * 删除客户端
 */
export function delClient(ids) {
  return request({
    url: '/admin/api/v1/oauth/client/' + ids,
    method: 'delete'
  })
}
