import request from '@/utils/request'

// 保存配置
export function saveConfig(data) {
  return request({
    url: '/admin/api/v1/oauth/config/save',
    method: 'post',
    data: data
  })
}

// 获取配置
export function getConfig(appType) {
  return request({
    url: '/admin/api/v1/oauth/config/get/' + appType,
    method: 'get'
  })
}

// 用户列表
export function listUser(data) {
  return request({
    url: '/admin/api/v1/oauth/user/list',
    method: 'post',
    data: data
  })
}

// 修改用户角色
export function changeUserRole(userId, roleId) {
  return request({
    url: '/admin/api/v1/oauth/user/role/change?userId=' + userId + '&roleId=' + roleId,
    method: 'get'
  })
}

// 修改用户角色
export function deleteUser(userId) {
  return request({
    url: '/admin/api/v1/oauth/user/delete?userId=' + userId,
    method: 'get'
  })
}
