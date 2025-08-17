import request from '@/utils/request'

/**
 * 文件上传
 */
export function uploadAttach(data) {
  return request({
    url: '/admin/api/v1/attach',
    method: 'post',
    data: data
  })
}

/**
 * 文件预览
 */
export function preViewAttach(attachId) {
  return request({
    url: '/admin/api/v1/attach/preview/' + attachId,
    method: 'get'
  })
}

/**
 * 文件列表
 */
export function listAttach(params) {
  return request({
    url: '/admin/api/v1/attach',
    method: 'get',
    params: params
  })
}

/**
 * 删除文件
 */
export function deleteAttach(attachIds) {
  return request({
    url: '/admin/api/v1/attach/' + attachIds,
    method: 'delete'
  })
}
