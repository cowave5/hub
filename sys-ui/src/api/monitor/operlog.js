import request from '@/utils/request'

/**
 * 列表
 */
export function listOpLog(params) {
  return request({
    url: '/admin/api/v1/oplog',
    method: 'get',
    params: params
  })
}

/**
 * 删除
 */
export function delOpLog(ids) {
  return request({
    url: '/admin/api/v1/oplog/' + ids,
    method: 'delete'
  })
}

/**
 * 清空
 */
export function cleanOpLog() {
  return request({
    url: '/admin/api/v1/oplog/clean',
    method: 'delete'
  })
}
