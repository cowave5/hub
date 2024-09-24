import request from '@/utils/request'

// 我的待办
export function listMyTask(data) {
  return request({
    url: '/admin/api/v1/flow/task/list',
    method: 'post',
    data: data
  })
}

// 全部待办
export function listAllTask(data) {
  return request({
    url: '/admin/api/v1/flow/task/list/all',
    method: 'post',
    data: data
  })
}

// 我办理的
export function listHistoryTask(data) {
  return request({
    url: '/admin/api/v1/flow/task/list/history',
    method: 'post',
    data: data
  })
}

// 任务办理
export function taskComplete(data) {
  return request({
    url: '/admin/api/v1/flow/task/complete',
    method: 'post',
    data: data
  })
}

// 办理记录
export function taskRecords(taskId) {
  return request({
    url: '/admin/api/v1/flow/task/records/' + taskId,
    method: 'get'
  })
}

// 修改办理人
export function taskAssignee(taskId, userId) {
  return request({
    url: '/admin/api/v1/flow/task/assignee/' + taskId + '/' + userId,
    method: 'get'
  })
}

// 催办
export function taskPress(taskId) {
  return request({
    url: '/admin/api/v1/flow/task/press/' + taskId,
    method: 'get'
  })
}
