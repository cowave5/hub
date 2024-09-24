import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/admin/api/v1/menu/list',
    method: 'get',
    params: query
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/admin/api/v1/menu/add',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/admin/api/v1/menu/edit',
    method: 'post',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/admin/api/v1/menu/delete?menuId=' + menuId,
    method: 'get'
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/admin/api/v1/menu/info/' + menuId,
    method: 'get'
  })
}

// 菜单只读修改
export function changeReadonly(menuId, readOnly, menuName) {
  const data = {
    menuId,
    readOnly,
    menuName
  }
  return request({
    url: '/admin/api/v1/menu/change/readonly',
    method: 'post',
    data: data
  })
}

// 查询菜单下拉树结构
export function treeselect() {
  return request({
    url: '/admin/api/v1/menu/tree',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

