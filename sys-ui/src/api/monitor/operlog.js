import request from '@/utils/request'

// 日志列表
export function list(data) {
  return request({
    url: '/admin/api/v1/log/list',
    method: 'post',
    data: data
  })
}

export function options() {
  return request({
    url: '/admin/api/v1/dict/group/options?groupCode=sys_log',
    method: 'get'
  })
}

// 日志详情
export function info(id) {
  return request({
    url: '/admin/api/v1/log/info/' + id,
    method: 'get'
  })
}

// 删除操作日志
export function delOperlog(id) {
  return request({
    url: '/admin/api/v1/log/delete?id=' + id,
    method: 'get'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: '/admin/api/v1/log/clean',
    method: 'get'
  })
}

export function userQuery(data) {
  return request({
    url: '/admin/api/v1/log/user/query',
    method: 'post',
    data: data
  })
}

export function deptQuery(data) {
  return request({
    url: '/admin/api/v1/log/dept/query',
    method: 'post',
    data: data
  })
}

export function postQuery(data) {
  return request({
    url: '/admin/api/v1/log/post/query',
    method: 'post',
    data: data
  })
}
