import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/ldap',
    component: () => import('@/views/ldap'),
    hidden: true
  },
  {
    path: '/oauth/gitlab',
    component: () => import('@/views/oauth'),
    hidden: true
  },
  {
    path: '/oauth/authorize',
    component: () => import('@/views/authorize.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title:  'commons.menu.dashboard', icon: 'index', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'commons.theme.profile', icon: 'user' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'notice',
        component: () => import('@/views/profile/notice/index'),
        name: 'notice',
        meta: { title: 'commons.theme.notice', icon: 'notice' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'token',
        component: () => import('@/views/profile/token/index'),
        name: 'token',
        meta: { title: 'commons.theme.token', icon: 'token' }
      }
    ]
  },
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/userRole.vue'),
        name: 'AuthRole',
        meta: { title: 'user.button.grant', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/roleUser.vue'),
        name: 'AuthUser',
        meta: { title: 'role.button.members', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dept-user',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'dept/:deptId(\\d+)',
        component: () => import('@/views/system/dept/deptUser'),
        name: 'DeptUser',
        meta: { title: 'dept.button.members', activeMenu: '/system/dept' }
      }
    ]
  },
  {
    path: '/system/dept-post',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'dept/:deptId(\\d+)',
        component: () => import('@/views/system/dept/deptPost'),
        name: 'DeptPost',
        meta: { title: 'dept.button.positions', activeMenu: '/system/dept' }
      }
    ]
  },
  {
    path: '/system/job-log',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/system/job/log'),
        name: 'JobLog',
        meta: { title: '任务日志', activeMenu: '/system/job' }
      }
    ]
  },
  {
    path: '/flow/manage-modeler',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'designer/:id',
        component: () => import('@/views/flow/designer'),
        name: 'flowDesigner',
        meta: { title: '流程设计', activeMenu: '/flow/manage/modeler' }
      }
    ]
  },
  {
    path: '/meter/template-column',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'table/:tableId(\\d+)',
        component: () => import('@/views/meter/template/column.vue'),
        name: 'tableColumn',
        meta: { title: '库表字段', activeMenu: '/meter/template/table' }
      }
    ]
  },
  {
    path: '/meter/template-field',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'model/:modelId(\\d+)',
        component: () => import('@/views/meter/template/field.vue'),
        name: 'ModelField',
        meta: { title: '模型字段', activeMenu: '/meter/template/model' }
      }
    ]
  },
]

// 权限路由
export const dynamicRoutes = [

]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
