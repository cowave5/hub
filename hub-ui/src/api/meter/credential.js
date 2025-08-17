import request from '@/utils/request'

/**
 * 凭据列表
 */
export function credentialList(params) {
  return request({
    url: '/meter/api/v1/credential',
    method: 'get',
    params: params
  })
}

/**
 * 凭据详情
 */
export function credentialInfo(credentialId) {
  return request({
    url: '/meter/api/v1/credential/' + credentialId,
    method: 'get'
  })
}

/**
 * 凭据新增
 */
export function credentialAdd(data) {
  return request({
    url: '/meter/api/v1/credential',
    method: 'post',
    data: data
  })
}

/**
 * 凭据删除
 */
export function credentialDel(credentialIds) {
  return request({
    url: '/meter/api/v1/credential/' + credentialIds,
    method: 'delete'
  })
}

/**
 * 凭据修改
 */
export function credentialUpdate(data) {
  return request({
    url: '/meter/api/v1/credential',
    method: 'patch',
    data: data
  })
}
