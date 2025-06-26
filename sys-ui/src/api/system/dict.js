import request from '@/utils/request'

/**
 * 字典列表
 */
export function getDictList(params) {
  return request({
    url: '/admin/api/v1/dict',
    method: 'get',
    params: params
  })
}

/**
 * 字典详情
 */
export function getDictInfo(dictId) {
  return request({
    url: '/admin/api/v1/dict/' + dictId,
    method: 'get'
  })
}

/**
 * 新增字典
 */
export function addDict(data) {
  return request({
    url: '/admin/api/v1/dict',
    method: 'post',
    data: data
  })
}

/**
 * 删除字典
 */
export function delDict(dictIds) {
  return request({
    url: '/admin/api/v1/dict/' + dictIds,
    method: 'delete'
  })
}

/**
 * 修改字典
 */
export function updateDict(data) {
  return request({
    url: '/admin/api/v1/dict',
    method: 'patch',
    data: data
  })
}

/**
 * 重置缓存
 */
export function refreshDict() {
  return request({
    url: '/admin/api/v1/dict/reset',
    method: 'get'
  })
}

/**
 * 获取类型字典
 */
export function getDictByType(dictType) {
  return request({
    url: '/admin/api/v1/dict/type/' + dictType,
    method: 'get'
  })
}
