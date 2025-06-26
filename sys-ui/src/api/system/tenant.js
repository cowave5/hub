import request from '@/utils/request'

/**
 * 租户列表
 */
export function listTenant(params) {
  return request({
    url: '/admin/api/v1/tenant',
    method: 'get',
    params: params
  })
}

/**
 * 租户详情
 */
export function getTenant(tenantId) {
  return request({
    url: '/admin/api/v1/tenant/' + tenantId,
    method: 'get'
  })
}

/**
 * 租户新增
 */
export function addTenant(data) {
  return request({
    url: '/admin/api/v1/tenant',
    method: 'post',
    data: data
  })
}

/**
 * 租户修改
 */
export function editTenant(data) {
  return request({
    url: '/admin/api/v1/tenant',
    method: 'patch',
    data: data
  })
}

/**
 * 租户修改
 */
export function switchTenantStatus(tenantId, tenantName, status){
  const data = {
    tenantId,
    tenantName,
    status
  }
  return request({
    url: '/admin/api/v1/tenant/status',
    method: 'patch',
    data: data
  })
}

/**
 * 管理员列表
 */
export function listTenantManager(tenantId, params) {
  return request({
    url: '/admin/api/v1/tenant/manager/' + tenantId,
    method: 'get',
    params: params
  })
}

/**
 * 创建管理员
 */
export function createTenantManager(data) {
  return request({
    url: '/admin/api/v1/tenant/manager',
    method: 'post',
    data: data
  })
}

/**
 * 移除管理员
 */
export function removeTenantManager(data) {
  return request({
    url: '/admin/api/v1/tenant/manager/remove',
    method: 'patch',
    data: data
  })
}

/**
 * 租户选项
 */
export function listTenantOptions() {
  return request({
    url: '/admin/api/v1/tenant/options',
    method: 'get'
  })
}
