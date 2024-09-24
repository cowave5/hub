import request from '@/utils/request'

// 查询岗位列表
export function listPost(data) {
  return request({
    url: '/admin/api/v1/post/list',
    method: 'post',
    data: data
  })
}

// 岗位关系
export function getTree() {
  return request({
    url: '/admin/api/v1/post/tree',
    method: 'get'
  })
}

// 刷新缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/post/refresh',
    method: 'get'
  })
}

// 岗位详情
export function getPost(postId) {
  return request({
    url: '/admin/api/v1/post/info/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: '/admin/api/v1/post/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: '/admin/api/v1/post/edit',
    method: 'post',
    data: data
  })
}

// 删除岗位
export function delPost(postId) {
  return request({
    url: '/admin/api/v1/post/delete?postId=' + postId,
    method: 'get'
  })
}

// 岗位只读修改
export function changeReadonly(postId, readOnly, postName) {
  const data = {
    postId,
    readOnly,
    postName
  }
  return request({
    url: '/admin/api/v1/post/change/readonly',
    method: 'post',
    data: data
  })
}

// 获取岗位人员
export function getPostUsersByCode(postCode) {
  return request({
    url: '/admin/api/v1/post/users/code/' + postCode,
    method: 'get'
  })
}
