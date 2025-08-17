import request from '@/utils/request'

/**
 * 目录结构
 */
export function buildFolderTree() {
  return request({
    url: '/meter/api/v1/build/folder',
    method: 'get'
  })
}

/**
 * 目录拖拽
 */
export function buildFolderDrag(data) {
  return request({
    url: '/meter/api/v1/build/folder/drag',
    method: 'patch',
    data: data
  })
}

/**
 * 新建目录
 */
export function creatBuildFolder(data) {
  return request({
    url: '/meter/api/v1/build/folder',
    method: 'post',
    data: data
  })
}

/**
 * 删除目录
 */
export function deleteBuildFolder(folderId) {
  return request({
    url: '/meter/api/v1/build/folder/' + folderId,
    method: 'delete'
  })
}

/**
 * 修改目录名称
 */
export function buildFolderRename(data) {
  return request({
    url: '/meter/api/v1/build/folder/name',
    method: 'patch',
    data: data
  })
}

/**
 * 修改访问限制
 */
export function buildFolderVisibility(data) {
  return request({
    url: '/meter/api/v1/build/folder/visibility',
    method: 'patch',
    data: data
  })
}

/**
 * 目录成员列表
 */
export function buildFolderMembers(folderId) {
  return request({
    url: '/meter/api/v1/build/folder/members/' + folderId,
    method: 'get'
  })
}

/**
 * 目录成员选项
 */
export function buildFolderSelectList(params) {
  return request({
    url: '/meter/api/v1/build/folder/members',
    method: 'get',
    params: params
  })
}

/**
 * 添加目录成员
 */
export function buildFolderMembersAdd(data) {
  return request({
    url: '/meter/api/v1/build/folder/members',
    method: 'post',
    data: data
  })
}

/**
 * 修改成员角色
 */
export function buildFolderMembersUpdate(data) {
  return request({
    url: '/meter/api/v1/build/folder/members/role',
    method: 'patch',
    data: data
  })
}

/**
 * 转让目录
 */
export function buildFolderMembersTransfer(data) {
  return request({
    url: '/meter/api/v1/build/folder/members/transfer',
    method: 'patch',
    data: data
  })
}

/**
 * 删除成员
 */
export function buildFolderMembersDelete(params) {
  return request({
    url: '/meter/api/v1/build/folder/members',
    method: 'delete',
    params: params
  })
}
