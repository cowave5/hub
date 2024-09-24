import request from '@/utils/request'

// 列表
export function listLdap(data) {
  return request({
    url: '/admin/api/v1/ldap/list',
    method: 'post',
    data: data
  })
}

// 详情
export function infoLdap(id) {
  return request({
    url: '/admin/api/v1/ldap/info/' + id,
    method: 'get'
  })
}

// 新增
export function addLdap(data) {
  return request({
    url: '/admin/api/v1/ldap/add',
    method: 'post',
    data: data
  })
}

// 更新
export function updateLdap(data) {
  return request({
    url: '/admin/api/v1/ldap/edit',
    method: 'post',
    data: data
  })
}

// 删除
export function delLdap(id) {
  return request({
    url: '/admin/api/v1/ldap/delete?id=' + id,
    method: 'get'
  })
}

// 修改状态
export function changeStatus(id, status) {
  return request({
    url: '/admin/api/v1/ldap/changeStatus?id=' + id + '&status=' + status,
    method: 'get'
  })
}

// 测试
export function validLdap(data) {
  return request({
    url: '/admin/api/v1/ldap/valid',
    method: 'post',
    data: data
  })
}

// 详情
export function infoUser(userId) {
  return request({
    url: '/admin/api/v1/ldap/user/' + userId,
    method: 'get'
  })
}
