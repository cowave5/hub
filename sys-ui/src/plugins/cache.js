import  jwt  from  'jsonwebtoken'

const UserId = 'user_id'
const UserCode = 'user_code'
const UserName = 'user_name'
const AccessKey = 'access_token'
const RefreshKey = 'refresh_token'

// 会话缓存
const sessionCache = {
  set (key, value) {
    if (!sessionStorage) {
      return
    }
    if (key != null && value != null) {
      sessionStorage.setItem(key, value)
    }
  },
  get (key) {
    if (!sessionStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return sessionStorage.getItem(key)
  },
  setJSON (key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },
  getJSON (key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
  remove (key) {
    sessionStorage.removeItem(key);
  }
}

// 本地缓存
const localCache = {
  set (key, value) {
    if (!localStorage) {
      return
    }
    if (key != null && value != null) {
      localStorage.setItem(key, value)
    }
  },

  get (key) {
    if (!localStorage) {
      return null
    }
    if (key == null) {
      return null
    }
    return localStorage.getItem(key)
  },

  remove (key) {
    localStorage.removeItem(key);
  },

  setToken(token){
    let accessInfo = jwt.decode(token.accessToken);
    this.set(UserId, accessInfo['User.id']);
    this.set(UserCode, accessInfo['User.code']);
    this.set(UserName, accessInfo['User.name']);
    this.set(AccessKey, token.accessToken);
    this.set(RefreshKey, token.refreshToken);
  },

  getUserId(){
    return this.get(UserId);
  },

  getUserCode(){
    return this.get(UserCode);
  },

  getUserName(){
    return this.get(UserName);
  },

  getAccessToken(){
    return this.get(AccessKey);
  },

  getRefreshToken(){
    return this.get(RefreshKey);
  },

  removeAccessToken(){
    this.remove(AccessKey);
  },

  setJSON (key, jsonValue) {
    if (jsonValue != null) {
      this.set(key, JSON.stringify(jsonValue))
    }
  },

  getJSON (key) {
    const value = this.get(key)
    if (value != null) {
      return JSON.parse(value)
    }
  },
}

export default {
  /**
   * 会话级缓存
   */
  session: sessionCache,
  /**
   * 本地缓存
   */
  local: localCache
}
