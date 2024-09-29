import request from '@/utils/request'

// Excel转换器
export function excelConverter() {
  return request({
    url: '/meter/api/v1/field//converter/excel',
    method: 'get'
  })
}

// 类型
export function typesField() {
  return request({
    url: '/meter/api/v1/field/types',
    method: 'get'
  })
}

// 列表
export function listField(modelId) {
  return request({
    url: '/meter/api/v1/field/list/' + modelId,
    method: 'get'
  })
}

// 新增
export function addField(data) {
  return request({
    url: '/meter/api/v1/field/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateField(data) {
  return request({
    url: '/meter/api/v1/field/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoField(id) {
  return request({
    url: '/meter/api/v1/field/info/' + id,
    method: 'get'
  })
}

// 删除
export function delField(ids) {
  return request({
    url: '/meter/api/v1/field/delete?id=' + ids,
    method: 'get'
  })
}

// 是否非空字段切换
export function switchNotnull(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/notnull/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否集合字段切换
export function switchCollect(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/collect/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否Excel字段切换
export function switchExcel(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/excel/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否where条件切换
export function switchWhere(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/where/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否列表字段切换
export function switchList(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/list/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否详情字段切换
export function switchInfo(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/info/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否新增字段切换
export function switchInsert(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/insert/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否修改字段切换
export function switchEdit(id, flag) {
  return request({
    url: '/meter/api/v1/field/switch/edit/' + id + '/' + flag,
    method: 'get'
  })
}
