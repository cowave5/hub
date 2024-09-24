import request from '@/utils/request'

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: '/admin/api/v1/dict/info/' + dictId,
    method: 'get'
  })
}

// 查询字典类型列表
export function listType(data) {
  return request({
    url: '/admin/api/v1/dict/list',
    method: 'get',
    params: data
  })
}

// 获取字典选择
export function optionselect() {
  return request({
    url: '/admin/api/v1/dict/options',
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: '/admin/api/v1/dict/add',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: '/admin/api/v1/dict/edit',
    method: 'post',
    data: data
  })
}

// 删除字典类型
export function delType(dictId) {
  return request({
    url: '/admin/api/v1/dict/delete?dictId=' + dictId,
    method: 'get'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/dict/refresh',
    method: 'get'
  })
}

// 菜单只读修改
export function changeReadonly(id, readOnly, dictCode) {
  const data = {
    id,
    readOnly,
    dictCode
  }
  return request({
    url: '/admin/api/v1/dict/change/readonly',
    method: 'post',
    data: data
  })
}


