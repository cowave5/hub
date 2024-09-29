 /**
 * v-hasPermi 操作权限处理
 * Copyright (c) 2019 ruoyi
 */

import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    const permissions = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissions.some(permission => {
        if (!permission) {
         return false;
        }
        return permissionFlag.some(flag => matchPermit(permission, flag));
      })

      if (!hasPermissions) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
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
