import request from '@/utils/request'

// 流程列表
export function instanceList(data) {
  return request({
    url: '/admin/api/v1/flow/instance/list',
    method: 'post',
    data: data
  })
}

// 流程进度
export function instanceProgress(id) {
  return request({
    url: '/admin/api/v1/flow/instance/progress/' + id,
    method: 'get'
  })
}

// 流程历史
export function instanceHistory(data) {
  return request({
    url: '/admin/api/v1/flow/instance/history',
    method: 'post',
    data: data
  })
}

// 流程变量
export function instanceVariable(data) {
  return request({
    url: '/admin/api/v1/flow/instance/variables',
    method: 'post',
    data: data
  })
}

// 流程跳转
export function instanceJump(taskId, tid, comment) {
  return request({
    url: '/admin/api/v1/flow/instance/jump/' + taskId + '/' + tid + '?comment=' + comment,
    method: 'get'
  })
}

// 流程跳转
export function instanceVarEdit(data) {
  return request({
    url: '/admin/api/v1/flow/instance/variables/edit',
    method: 'post',
    data: data
  })
}

// 流程跳转
export function instanceVarDelete(data) {
  return request({
    url: '/admin/api/v1/flow/instance/variables/delete',
    method: 'post',
    data: data
  })
}

// 流程删除
export function instanceDelete(id) {
  return request({
    url: '/admin/api/v1/flow/instance/delete/' + id,
    method: 'get'
  })
}

// 流程挂起
export function instanceSuspend(id) {
  return request({
    url: '/admin/api/v1/flow/instance/suspend/' + id,
    method: 'get'
  })
}

// 流程激活
export function instanceActivate(id) {
  return request({
    url: '/admin/api/v1/flow/instance/activate/' + id,
    method: 'get'
  })
}
