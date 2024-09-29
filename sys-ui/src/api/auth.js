import request from '@/utils/request'

/**
 * 获取验证码
 */
export function getCodeImg() {
  return request({
    url: '/admin/api/v1/auth/public/captcha',
    method: 'get',
    timeout: 20000,
    headers: {
      requireToken: false
    },
  })
}

/**
 * 发送邮箱验证码
 */
export function getEmailCode(email) {
  return request({
    url: '/admin/api/v1/auth/public/captcha/email?email=' + email,
    method: 'get',
    timeout: 20000,
    headers: {
      requireToken: false
    },
  })
}

/**
 * 注册
 */
export function register(data) {
  return request({
    url: '/admin/api/v1/auth/public/register',
    method: 'post',
    data: data,
    headers: {
      requireToken: false
    },
  })
}

/**
 * 登录
 */
export function login(userAccount, passWord, code, uuid) {
  const data = {
    userAccount,
    passWord,
    "captcha" : code,
    "captchaId" : uuid
  }
  return request({
    url: '/admin/api/v1/auth/public/login',
    method: 'post',
    data: data,
    headers: {
      requireToken: false
    },
  })
}

/**
 * Ldap登录
 */
export function ldapLogin(userAccount, passWord) {
  const data = {
    userAccount,
    passWord
  }
  return request({
    url: '/admin/api/v1/auth/public/ldap',
    method: 'post',
    data: data,
    headers: {
      requireToken: false
    },
  })
}

/**
 * 登录
 */
export function gitlabLogin(code) {
  return request({
    url: '/admin/api/v1/auth/public/gitlab?code=' + code,
    method: 'get',
    headers: {
      requireToken: false
    },
  })
}

/**
 * 退出登录
 */
export function logout() {
  return request({
    url: '/admin/api/v1/auth/logout',
    method: 'get'
  })
}

/**
 * 刷新令牌
 */
export function refresh(token) {
  return request({
    url: '/admin/api/v1/auth/public/refresh?refreshToken=' + token,
    method: 'get'
  })
}

/**
 * 获取登录信息
 */
export function getAuthInfo() {
  return request({
    url: '/admin/api/v1/auth/info',
    method: 'get'
  })
}

/**
 * 获取权限路由
 */
export const getAuthRouters = () => {
  return request({
    url: '/admin/api/v1/auth/routes',
    method: 'get'
  })
}

/**
 * 获取OAuth2授权码
 */
export const getOAuth2Code = (data) => {
  return request({
    url: '/admin/api/v1/oauth/client/authorize/code',
    method: 'post',
    data: data
  })
}

/**
 * 获取OAuth2授权码
 */
export const getOAuth2Token = (data) => {
  return request({
    url: '/admin/api/v1/oauth/client/authorize/token',
    method: 'post',
    data: data,
    headers: {
      requireToken: false
    },
  })
}
