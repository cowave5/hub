import store from '@/store'

/**
 * 字符权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkPermit(value) {
  if (value && value instanceof Array && value.length > 0) {
    const permissions = store.getters && store.getters.permissions
    const permissionDatas = value
    return permissions.some(permission => {
      if (!permission) {
        return false;
      }
      return permissionDatas.some(flag => matchPermit(permission, flag));
    });
  } else {
    console.error(`need roles! Like checkPermi="['']"`)
    return false
  }
}

function matchPermit(srcPermit, destPermit) {
  const src = srcPermit.split(":");
  const dest = destPermit.split(":");
  for (let i = 0; i < src.length; i++) {
    if (src[i] === "*") {
      return true;
    }
    if (i >= dest.length) {
      return false;
    }
    if (src[i] !== dest[i]) {
      return false;
    }
  }
  return true;
}

/**
 * 角色权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkRole(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.roles
    const permissionRoles = value
    return roles.some(role => {
      return permissionRoles.includes(role)
    });
  } else {
    console.error(`need roles! Like checkRole="['admin','editor']"`)
    return false
  }
}
