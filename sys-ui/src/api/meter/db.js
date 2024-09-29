import request from '@/utils/request'

// 选项
export function optionsDb(projectId) {
  let url = '/meter/api/v1/db/options';
  if(projectId != null){
    url = '/meter/api/v1/db/options?projectId=' + projectId;
  }
  return request({
    url: url,
    method: 'get'
  })
}

// 列表
export function listDb(data) {
  return request({
    url: '/meter/api/v1/db/list',
    method: 'post',
    data: data
  })
}

// 新增
export function addDb(data) {
  return request({
    url: '/meter/api/v1/db/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateDb(data) {
  return request({
    url: '/meter/api/v1/db/edit',
    method: 'post',
    data: data
  })
}

// 详细
export function infoDb(id) {
  return request({
    url: '/meter/api/v1/db/info/' + id,
    method: 'get'
  })
}

// 删除
export function delDb(ids) {
  return request({
    url: '/meter/api/v1/db/delete?id=' + ids,
    method: 'get'
  })
}

// 从数据库同步表信息
export function synTable(data) {
  return request({
    url: '/meter/api/v1/db/synTable',
    method: 'post',
    data: data
  })
}

// DDL预览
export function preview(id) {
  return request({
    url: '/meter/api/v1/db/preview/' + id,
    method: 'get'
  })
}
