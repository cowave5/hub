import request from '@/utils/request'

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: '/admin/api/v1/dict/cache/type?typeCode=' + dictType,
    method: 'get'
  })
}

// 查询字典数据列表
export function listData(data) {
  return request({
    url: '/admin/api/v1/dict/list',
    method: 'get',
    params: data
  })
}

// 查询字典数据
export function getData(dictCode) {
  return request({
    url: '/admin/api/v1/dict/info/' + dictCode,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: '/admin/api/v1/dict/add',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: '/admin/api/v1/dict/edit',
    method: 'post',
    data: data
  })
}

// 删除字典数据
export function delData(dictCode) {
  return request({
    url: '/admin/api/v1/dict/delete?dictId=' + dictCode,
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
