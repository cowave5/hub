import request from '@/utils/request'

// 选项
export function optionsModel(ids) {
  return request({
    url: '/code/api/v1/model/options',
    method: 'get'
  })
}

// 列表
export function listModel(data) {
  return request({
    url: '/code/api/v1/model/list',
    method: 'post',
    data: data
  })
}

// 新增
export function addModel(data) {
  return request({
    url: '/code/api/v1/model/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateModel(data) {
  return request({
    url: '/code/api/v1/model/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoModel(id) {
  return request({
    url: '/code/api/v1/model/info/' + id,
    method: 'get'
  })
}

// 删除
export function delModel(ids) {
  return request({
    url: '/code/api/v1/model/delete?id=' + ids,
    method: 'get'
  })
}

// 是否导出Excel切换
export function switchExcel(id, flag) {
  return request({
    url: '/code/api/v1/model/switch/excel/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否继承Access切换
export function switchAccess(id, flag) {
  return request({
    url: '/code/api/v1/model/switch/access/' + id + '/' + flag,
    method: 'get'
  })
}

// 是否记录日志切换
export function switchLog(id, flag) {
  return request({
    url: '/code/api/v1/model/switch/log/' + id + '/' + flag,
    method: 'get'
  })
}

// 生成模型
export function generate(data) {
  return request({
    url: '/code/api/v1/model/generate',
    method: 'post',
    data: data
  })
}

// 代码预览
export function preview(id) {
  return request({
    url: '/code/api/v1/model/preview/' + id,
    method: 'get'
  })
}
