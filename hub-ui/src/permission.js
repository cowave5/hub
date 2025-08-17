import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import cache from "@/plugins/cache";

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/open/login', '/cowave/login', '/cowave/register', '/cowave/ldap', '/oauth/gitlab', '/bind']

// 路由跳转
router.beforeEach((to, from, next) => {
  NProgress.start()
  // 检查AccessToken（本地持久化）
  if (cache.local.getAccessToken()) {
    // 直接将next置为首页
    if (to.path === '/mfa'
        || to.path === '/login'
        || to.path === '/open/login'
        || to.path === '/cowave/login'
        || to.path === '/cowave/ldap'
        || to.path === '/oauth/gitlab') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // 检查用户权限信息（缓存），没有就重新获取
      if (store.getters.userId == null) {
        store.dispatch('GetInfo').then(() => {
          // 重新获取路由菜单权限
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            router.addRoutes(accessRoutes)
            next({ ...to, replace: true })
          })
        }).catch(err => {
          cache.local.removeAccessToken();
          const loginRoute = localStorage.getItem('tenant_login_route') || '/cowave/login';
          next({ path: loginRoute });
        })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else if(to.path === '/mfa' && cache.local.getMfa()) {
      next()
    } else {
      const redirect = encodeURIComponent(to.fullPath);
      const loginRoute = localStorage.getItem('tenant_login_route') || '/cowave/login';
      next(`${loginRoute}?redirect=${redirect}`);
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
