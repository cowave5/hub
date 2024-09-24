import request from '@/utils/request'

// 模型列表
export function listModel(data) {
  return request({
    url: '/admin/api/v1/flow/model/list',
    method: 'post',
    data: data
  })
}

// 新增模型
export function addModel(data) {
  return request({
    url: '/admin/api/v1/flow/model/add',
    method: 'post',
    data: data
  })
}

// 发布模型
export function deployModel(modelId) {
  return request({
    url: '/admin/api/v1/flow/model/deploy/' + modelId,
    method: 'get'
  })
}

// 删除模型
export function deleteModel(modelId) {
  return request({
    url: '/admin/api/v1/flow/model/delete/' + modelId,
    method: 'get'
  })
}
