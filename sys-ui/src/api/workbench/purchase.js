import request from '@/utils/request'

// 采购列表
export function listPurchase(data) {
  return request({
    url: '/admin/api/v1/flow/purchase/list',
    method: 'post',
    data: data
  })
}

// 请假详情
export function infoPurchase(id) {
  return request({
    url: '/admin/api/v1/flow/purchase/info/' + id,
    method: 'get'
  })
}

// 新增采购
export function addPurchase(data) {
  return request({
    url: '/admin/api/v1/flow/purchase/add',
    method: 'post',
    data: data
  })
}

// 修改采购
export function editPurchase(data) {
  return request({
    url: '/admin/api/v1/flow/purchase/edit',
    method: 'post',
    data: data
  })
}

// 删除采购
export function delPurchase(ids) {
  return request({
    url: '/admin/api/v1/flow/purchase/delete/' + ids,
    method: 'get'
  })
}
