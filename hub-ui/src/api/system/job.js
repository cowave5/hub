import request from '@/utils/request'

/**
 * 任务列表
 */
export function listJob(params) {
  return request({
    url: '/job/api/v1/trigger',
    method: 'get',
    params: params
  })
}

/**
 * 任务新增
 */
export function addJob(data) {
  return request({
    url: '/job/api/v1/trigger',
    method: 'post',
    data: data
  })
}

/**
 * 任务删除
 */
export function delJob(ids) {
  return request({
    url: '/job/api/v1/trigger/' + ids,
    method: 'delete'
  })
}

/**
 * 任务状态切换
 */
export function switchJobStatus(id, status) {
  return request({
    url: '/job/api/v1/trigger/status/' + id + '/' + status,
    method: 'patch'
  })
}

/**
 * 立即执行
 */
export function execJob(id) {
  return request({
    url: '/job/api/v1/trigger/exec/' + id,
    method: 'post'
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/quartz/api/v1/task/info/' + jobId,
    method: 'get'
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
