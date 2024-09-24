import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 岗位关系
export function getTree() {
  return request({
    url: '/admin/api/v1/user/tree',
    method: 'get'
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/admin/api/v1/user/refresh',
    method: 'get'
  })
}

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/admin/api/v1/user/list',
    method: 'post',
    data: query
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/admin/api/v1/user/info/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 角色选择
export function getRoles() {
  return request({
    url: '/admin/api/v1/role/list',
    method: 'post',
    data: {}
  })
}

// 岗位选择
export function getPosts(deptId) {
  return request({
    url: '/admin/api/v1/post/tree/dept',
    method: 'get'
  })
}

// 用户选择
export function getUsers() {
  return request({
    url: '/admin/api/v1/user/tree/dept',
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/admin/api/v1/user/add',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/admin/api/v1/user/edit',
    method: 'post',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  return request({
    url: '/admin/api/v1/user/delete?userId=' + userId,
    method: 'get'
  })
}

// 用户密码重置
export function resetUserPwd(userId, userPasswd, userName) {
  const data = {
    userId,
    userPasswd,
    userName
  }
  return request({
    url: '/admin/api/v1/user/change/passwd',
    method: 'post',
    data: data
  })
}

// 用户状态修改
export function changeStatus(userId, userStatus, userName) {
  const data = {
    userId,
    userName,
    userStatus
  }
  return request({
    url: '/admin/api/v1/user/change/status',
    method: 'post',
    data: data
  })
}

// 用户只读修改
export function changeReadonly(userId, readOnly, userName) {
  const data = {
    userId,
    readOnly,
    userName
  }
  return request({
    url: '/admin/api/v1/user/change/readonly',
    method: 'post',
    data: data
  })
}

// 用户角色修改
export function updateAuthRole(userId, roleIds) {
  const data = {
    userId,
    roleIds
  }
  return request({
    url: '/admin/api/v1/user/change/roles',
    method: 'post',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/admin/api/v1/profile/info',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/admin/api/v1/profile/edit',
    method: 'post',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPasswd, newPasswd) {
  const data = {
    oldPasswd,
    newPasswd
  }
  return request({
    url: '/admin/api/v1/profile/passwd/reset',
    method: 'post',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/admin/api/v1/profile/avatar',
    method: 'post',
    data: data
  })
}

// 用户部门领导
export function userLeaders(userId) {
  let url;
  if (userId === undefined) {
    url = '/admin/api/v1/user/leaders';
  } else {
    url = '/admin/api/v1/user/leaders?userId=' + userId;
  }
  return request({
    url: url,
    method: 'get'
  })
}

