import request from '@/utils/request'

// 类型
export function optionsColumn(modelId) {
  return request({
    url: '/meter/api/v1/column/options/' + modelId,
    method: 'get'
  })
}

// 类型
export function typesColumn(dbType) {
  return request({
    url: '/meter/api/v1/column/types/' + dbType,
    method: 'get'
  })
}

// 列表
export function listColumn(tableId) {
  return request({
    url: '/meter/api/v1/column/list/' + tableId,
    method: 'get',
  })
}

// 新增
export function addColumn(data) {
  return request({
    url: '/meter/api/v1/column/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateColumn(data) {
  return request({
    url: '/meter/api/v1/column/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoColumn(id) {
  return request({
    url: '/meter/api/v1/column/info/' + id,
    method: 'get'
  })
}

// 删除
export function delColumn(ids) {
  return request({
    url: '/meter/api/v1/column/delete?id=' + ids,
    method: 'get'
  })
}

// 非空
export function switchNotnull(id, flag) {
  return request({
    url: '/meter/api/v1/column/switch/notnull/' + id + '/' + flag,
    method: 'get'
  })
}

// 主键
export function switchPrimary(id, flag) {
  return request({
    url: '/meter/api/v1/column/switch/primary/' + id + '/' + flag,
    method: 'get'
  })
}

// 自增
export function switchIncrement(id, flag) {
  return request({
    url: '/meter/api/v1/column/switch/increment/' + id + '/' + flag,
    method: 'get'
  })
}
