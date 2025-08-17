import {login, logout, getAuthInfo, ldapLogin, gitlabLogin, logon, mfaLogin} from '@/api/auth'
import cache from "@/plugins/cache";

const user = {
  state: {
    accessToken: cache.local.getAccessToken(),
    refreshToken: cache.local.getRefreshToken(),
    tenantId: null,
    tenantTitle: null,
    tenantLogo: null,
    tenantIndex: null,
    userId: null,
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_MFA:  (state, data) => {
      cache.local.setMfa(data.accessToken)
    },
    SET_TOKEN: (state, token) => {
      cache.local.setToken(token);
    },
    SET_TENANT: (state, tenantId) => {
      state.tenantId = tenantId
    },
    SET_TENANT_TITLE: (state, tenantTitle) => {
      state.tenantTitle = tenantTitle
    },
    SET_TENANT_LOGO: (state, tenantLogo) => {
      state.tenantLogo = tenantLogo
    },
    SET_TENANT_INDEX: (state, tenantIndex) => {
      state.tenantIndex = tenantIndex;
      cache.local.setTenantIndex(tenantIndex);
    },
    SET_USERID: (state, userId) => {
      state.userId = userId
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
  },

  actions: {
    Logon({ commit }, userInfo) {
      const tenantId = userInfo.tenantId;
      const username = userInfo.username.trim();
      const password = userInfo.password;
      return new Promise((resolve, reject) => {
        logon(tenantId, username, password).then(res => {
          if (res.data.mfaRequired) {
            commit('SET_MFA', res.data);
            resolve({ mfaRequired: true });
          }else{
            cache.local.removeMfa();
            commit('SET_TOKEN', res.data);
            commit('SET_TENANT_INDEX', res.data.tenantIndex);
            resolve({ mfaRequired: false });
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    Login({ commit }, userInfo) {
      const tenantId = userInfo.tenantId;
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise((resolve, reject) => {
        login(tenantId, username, password, code, uuid).then(res => {
          if (res.data.mfaRequired) {
            commit('SET_MFA', res.data);
            resolve({ mfaRequired: true });
          }else{
            cache.local.removeMfa();
            commit('SET_TOKEN', res.data);
            commit('SET_TENANT_INDEX', res.data.tenantIndex);
            resolve({ mfaRequired: false });
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    Mfa({ commit }, mfaInfo) {
      return new Promise((resolve, reject) => {
        mfaLogin(mfaInfo).then(res => {
          cache.local.removeMfa();
          commit('SET_TOKEN', res.data);
          commit('SET_TENANT_INDEX', res.data.tenantIndex);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    Ldap({ commit }, ldapInfo) {
      const tenantId = ldapInfo.tenantId;
      const username = ldapInfo.username.trim()
      const password = ldapInfo.password
      return new Promise((resolve, reject) => {
        ldapLogin(tenantId, username, password).then(res => {
          cache.local.removeMfa();
          commit('SET_TOKEN', res.data);
          commit('SET_TENANT_INDEX', res.data.tenantIndex);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    OauthGitlab({ commit }, query) {
      return new Promise((resolve, reject) => {
        gitlabLogin(query.tenantId, query.code).then(res => {
          cache.local.removeMfa();
          commit('SET_TOKEN', res.data);
          commit('SET_TENANT_INDEX', res.data.tenantIndex);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },

    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          cache.local.removeAccessToken();
          commit('SET_TENANT', '')
          commit('SET_TENANT_TITLE', '')
          commit('SET_TENANT_LOGO', null)
          commit('SET_TENANT_TITLE', null)
          commit('SET_USERID', null)
          commit('SET_NAME', '')
          commit('SET_AVATAR', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getAuthInfo().then(res => {
          const user = res.data
          const avatar = (user.avatar === "" || user.avatar == null) ? require("@/assets/images/profile.jpg") : user.avatar;
          let logo;
          if (!user.tenantLogo) {
            if (user.tenantId === 'system') {
              logo = require("@/assets/logo/satellite.svg");
            }else if (user.tenantId === 'cowave') {
              logo = require("@/assets/logo/logo.png");
            }else{
              logo = require("@/assets/logo/sat.svg");
            }
          }else{
            logo = user.tenantLogo;
          }

          commit('SET_TENANT', user.tenantId)
          commit('SET_TENANT_TITLE', user.tenantTitle)
          commit('SET_TENANT_LOGO', logo)
          commit('SET_USERID', user.userId)
          commit('SET_NAME', user.userName)
          commit('SET_AVATAR', avatar)
          commit('SET_ROLES', user.roles)
          commit('SET_PERMISSIONS', user.permissions)
          commit('settings/SET_TITLE', user.tenantTitle)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

  }
}

export default user
