import request from '@/utils/request'

/**
 * 获取菜单树
 */
export function getMenuTree() {
  return request({
    url: '/admin/api/v1/menu/tree',
    method: 'get'
  })
}

/**
 * 菜单列表
 */
export function getMenuList(query) {
  return request({
    url: '/admin/api/v1/menu',
    method: 'get',
    params: query
  })
}

/**
 * 菜单详情
 */
export function getMenuInfo(menuId) {
  return request({
    url: '/admin/api/v1/menu/' + menuId,
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export function addMenu(data) {
  return request({
    url: '/admin/api/v1/menu',
    method: 'post',
    data: data
  })
}

/**
 * 删除菜单
 */
export function delMenu(menuId) {
  return request({
    url: '/admin/api/v1/menu/' + menuId,
    method: 'delete'
  })
}

/**
 * 修改菜单
 */
export function updateMenu(data) {
  return request({
    url: '/admin/api/v1/menu',
    method: 'patch',
    data: data
  })
}
