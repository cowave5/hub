import Cookies from 'js-cookie'
import  jwt  from  'jsonwebtoken'

// cookie的问题是它会在每次请求中携带，但是不希望这样

const UserId = 'user_id'
const UserName = 'user_name'
const AccessKey = 'access_token'
const RefreshKey = 'refresh_token'

export function getUserId() {
  return Cookies.get(UserId)
}

export function getUserName() {
  return Cookies.get(UserName)
}

export function getAccessToken() {
  return Cookies.get(AccessKey)
}

export function getRefreshToken() {
  return Cookies.get(RefreshKey)
}

export function setToken(token) {
  let accessInfo = jwt.decode(token.accessToken);
  Cookies.set(UserId, accessInfo['User.id'], { expires: 7});
  Cookies.set(UserName, accessInfo['User.name'], { expires: 7});
  Cookies.set(AccessKey, token.accessToken, { expires: 7});
  Cookies.set(RefreshKey, token.refreshToken, { expires: 7});
}

export function removeToken() {
  Cookies.remove(UserId);
  Cookies.remove(UserName);
  Cookies.remove(AccessKey);
  Cookies.remove(RefreshKey);
}
