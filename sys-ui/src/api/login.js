import request from '@/utils/request'

export function getCodeImg() {
  return request({
    url: '/admin/api/v1/auth/captcha',
    headers: {
      requireToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

export function getEmailCode(email) {
  return request({
    url: '/admin/api/v1/auth/captcha/email?email=' + email,
    headers: {
      requireToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

export function register(data) {
  return request({
    url: '/admin/api/v1/auth/register',
    headers: {
      requireToken: false
    },
    method: 'post',
    data: data
  })
}

export function login(userAccount, passWord, code, uuid) {
  const data = {
    userAccount,
    passWord,
    "captcha" : code,
    "captchaId" : uuid
  }
  return request({
    url: '/admin/api/v1/auth/login',
    headers: {
      requireToken: false
    },
    method: 'post',
    data: data
  })
}

export function logout() {
  return request({
    url: '/admin/api/v1/auth/logout',
    method: 'post'
  })
}

export function refresh(token) {
  return request({
    url: '/admin/api/v1/auth/refresh?refreshToken=' + token,
    method: 'get'
  })
}

export function getInfo() {
  return request({
    url: '/admin/api/v1/auth/info',
    method: 'get'
  })
}
