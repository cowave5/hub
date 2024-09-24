import request from '@/utils/request'

// 选项
export function optionsTable(appId) {
  return request({
    url: '/code/api/v1/table/options/' + appId,
    method: 'get'
  })
}

// 列表
export function listTable(data) {
  return request({
    url: '/code/api/v1/table/list',
    method: 'post',
    data: data
  })
}

// 新增
export function addTable(data) {
  return request({
    url: '/code/api/v1/table/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateTable(data) {
  return request({
    url: '/code/api/v1/table/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoTable(id) {
  return request({
    url: '/code/api/v1/table/info/' + id,
    method: 'get'
  })
}

// 删除
export function delTable(ids) {
  return request({
    url: '/code/api/v1/table/delete?id=' + ids,
    method: 'get'
  })
}

// DDL预览
export function preview(id) {
  return request({
    url: '/code/api/v1/table/preview/' + id,
    method: 'get'
  })
}
