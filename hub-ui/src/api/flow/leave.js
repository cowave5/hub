import request from '@/utils/request'

// 请假列表
export function listAllLeave(data) {
  return request({
    url: '/admin/api/v1/flow/leave/list',
    method: 'post',
    data: data
  })
}

// 我的请假
export function listMyLeave(data) {
  return request({
    url: '/admin/api/v1/flow/leave/list/my',
    method: 'post',
    data: data
  })
}

// 请假详情
export function infoLeave(id) {
  return request({
    url: '/admin/api/v1/flow/leave/info/' + id,
    method: 'get'
  })
}

// 新增请假
export function addLeave(data) {
  return request({
    url: '/admin/api/v1/flow/leave/add',
    method: 'post',
    data: data
  })
}

// 修改请假
export function editLeave(data) {
  return request({
    url: '/admin/api/v1/flow/leave/edit',
    method: 'post',
    data: data
  })
}

// 删除请假
export function delLeave(ids) {
  return request({
    url: '/admin/api/v1/flow/leave/delete/' + ids,
    method: 'get'
  })
}

// 撤销请假
export function revocateLeave(id) {
  return request({
    url: '/admin/api/v1/flow/leave/revocate/' + id,
    method: 'get'
  })
}
