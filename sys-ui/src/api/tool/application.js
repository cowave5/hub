import request from '@/utils/request'

// 列表
export function listApplication(data) {
  return request({
    url: '/code/api/v1/app/list',
    method: 'post',
    data: data
  })
}

// 新增
export function addApplication(data) {
  return request({
    url: '/code/api/v1/app/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateApplication(data) {
  return request({
    url: '/code/api/v1/app/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoApplication(id) {
  return request({
    url: '/code/api/v1/app/info/' + id,
    method: 'get'
  })
}

// 删除
export function delApplication(ids) {
  return request({
    url: '/code/api/v1/app/delete?id=' + ids,
    method: 'get'
  })
}
