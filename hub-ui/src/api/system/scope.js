import request from '@/utils/request'

/**
 * 权限列表
 */
export function listScope(params) {
  return request({
    url: '/admin/api/v1/scope',
    method: 'get',
    params: params
  })
}

/**
 * 权限详情
 */
export function intoScope(scopeId) {
  return request({
    url: '/admin/api/v1/scope/' + scopeId,
    method: 'get'
  })
}

/**
 * 权限新增
 */
export function addScope(data) {
  return request({
    url: '/admin/api/v1/scope',
    method: 'post',
    data: data
  })
}

/**
 * 权限删除
 */
export function deleteScope(scopeIds) {
  return request({
    url: '/admin/api/v1/scope/' + scopeIds,
    method: 'delete'
  })
}

/**
 * 权限修改
 */
export function editScope(data) {
  return request({
    url: '/admin/api/v1/scope',
    method: 'patch',
    data: data
  })
}

/**
 * 修改状态
 */
export function updateScopeStatus(scopeId, scopeStatus) {
  const data = {
    scopeId,
    scopeStatus
  }
  return request({
    url: '/admin/api/v1/scope/status',
    method: 'patch',
    data: data
  })
}

/**
 * 修改内容
 */
export function updateScopeContent(scopeId, scopeContent) {
  const data = {
    scopeId,
    scopeContent
  }
  return request({
    url: '/admin/api/v1/scope/content',
    method: 'patch',
    data: data
  })
}
