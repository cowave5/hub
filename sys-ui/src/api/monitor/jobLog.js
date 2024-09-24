import request from '@/utils/request'

// 查询调度日志列表
export function listJobLog(data) {
  return request({
    url: '/quartz/api/v1/task/log/list',
    method: 'post',
    data: data
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
