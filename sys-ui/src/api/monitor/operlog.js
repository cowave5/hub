import request from '@/utils/request'

/**
 * 模块类型选项
 */
export function getOpLogOptions() {
  return request({
    url: '/admin/api/v1/dict/group/types/op_log',
    method: 'get'
  })
}

/**
 * 列表
 */
export function getOpLogList(params) {
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
