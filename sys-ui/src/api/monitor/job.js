import request from '@/utils/request'

// 查询定时任务调度列表
export function listJob(query) {
  return request({
    url: '/quartz/api/v1/task/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/quartz/api/v1/task/info/' + jobId,
    method: 'get'
  })
}

// 重新加载
export function refreshJob() {
  return request({
    url: '/quartz/api/v1/task/refresh',
    method: 'get'
  })
}

// 立即执行一次
export function runJob(id) {
  return request({
    url: '/quartz/api/v1/task/exec?id=' + id,
    method: 'get'
  })
}

// 任务状态修改
export function changeJobStatus(id, status) {
  return request({
    url: '/quartz/api/v1/task/status/change?id=' + id + '&status=' + status,
    method: 'get'
  })
}

// 删除定时任务调度
export function delJob(jobId) {
  return request({
    url: '/quartz/api/v1/task/delete?id=' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data) {
  return request({
    url: '/quartz/api/v1/task/add',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateJob(data) {
  return request({
    url: '/quartz/api/v1/task/edit',
    method: 'post',
    data: data
  })
}
