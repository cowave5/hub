import request from '@/utils/request'

/**
 * 日志列表
 */
export function listJobLog(params) {
  return request({
    url: '/job/api/v1/trigger/log',
    method: 'get',
    params: params
  })
}

// 删除调度日志
export function delJobLog(jobLogId) {
  return request({
    url: '/quartz/api/v1/task/log/delete?id=' + jobLogId,
    method: 'get'
  })
}

// 清空
export function cleanJobLog() {
  return request({
    url: '/quartz/api/v1/task/log/clean',
    method: 'get'
  })
}
