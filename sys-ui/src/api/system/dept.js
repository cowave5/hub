import request from '@/utils/request'

/**
 * 部门列表
 */
export function getDeptList(params) {
  return request({
    url: '/admin/api/v1/dept',
    method: 'get',
    params: params
  })
}

/**
 * 部门详情
 */
export function getDeptInfo(deptId) {
  return request({
    url: '/admin/api/v1/dept/' + deptId,
    method: 'get'
  })
}

/**
 * 新增部门
 */
export function addDept(data) {
  return request({
    url: '/admin/api/v1/dept',
    method: 'post',
    data: data
  })
}

/**
 * 删除部门
 */
export function delDept(deptIds) {
  return request({
    url: '/admin/api/v1/dept/' + deptIds,
    method: 'delete'
  })
}

/**
 * 修改部门
 */
export function updateDept(data) {
  return request({
    url: '/admin/api/v1/dept',
    method: 'patch',
    data: data
  })
}

/**
 * 添加部门岗位
 */
export function addDeptPosts(data) {
  return request({
    url: '/admin/api/v1/dept/posts',
    method: 'post',
    data: data
  })
}

/**
 * 移除部门岗位
 */
export function removeDeptPosts(deptId, postIds) {
  return request({
    url: '/admin/api/v1/dept/posts/' + deptId + '/' + postIds,
    method: 'delete'
  })
}

/**
 * 获取部门岗位（已设置）
 */
export function getConfiguredPosts(params) {
  return request({
    url: '/admin/api/v1/dept/posts/configured',
    method: 'get',
    params: params
  })
}

/**
 * 获取部门岗位（未设置）
 */
export function getUnConfiguredPosts(params) {
  return request({
    url: '/admin/api/v1/dept/posts/unConfigured',
    method: 'get',
    params: params
  })
}

/**
 * 添加部门成员
 */
export function addDeptMembers(data) {
  return request({
    url: '/admin/api/v1/dept/members',
    method: 'post',
    data: data
  })
}

/**
 * 移除部门成员
 */
export function removeDeptMembers(deptId, userIds) {
  return request({
    url: '/admin/api/v1/dept/members/' + deptId + '/' + userIds,
    method: 'delete'
  })
}

/**
 * 获取部门成员（已加入）
 */
export function getJoinedMembers(params) {
  return request({
    url: '/admin/api/v1/dept/members/joined',
    method: 'get',
    params: params
  })
}

/**
 * 获取部门成员（未加入）
 */
export function getUnJoinedMembers(params) {
  return request({
    url: '/admin/api/v1/dept/members/unJoined',
    method: 'get',
    params: params
  })
}

/**
 * 部门岗位树
 */
export function getDeptPostDiagram() {
  return request({
    url: '/admin/api/v1/dept/diagram/post',
    method: 'get'
  })
}

/**
 * 部门用户树
 */
export function getDeptUserDiagram() {
  return request({
    url: '/admin/api/v1/dept/diagram/user',
    method: 'get'
  })
}

/**
 * 部门组织架构
 */
export function getDeptDiagram() {
  return request({
    url: '/admin/api/v1/dept/diagram',
    method: 'get'
  })
}

/**
 * 部门组织架构
 */
export function getDeptDiagramById(deptId) {
  return request({
    url: '/admin/api/v1/dept/diagram?deptId=' + deptId,
    method: 'get'
  })
}

/**
 * 刷新部门组织
 */
export function refreshDeptDiagram() {
  return request({
    url: '/admin/api/v1/dept/diagram/refresh',
    method: 'get'
  })
}

/**
 * 流程候选人列表
 */
export function getDeptCandidatesByCode(deptCode) {
  return request({
    url: '/admin/api/v1/dept/candidates/' + deptCode,
    method: 'get'
  })
}

/**
 * 查询部门名称
 */
export function getDeptNames(deptIds) {
  return request({
    url: '/admin/api/v1/dept/name/' + deptIds,
    method: 'get'
  })
}
