import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  // {
  //   path: '/login',
  //   component: () => import('@/views/login/index'),
  //   hidden: true
  // },

  {
    path: '/login',
    name: 'login',
    component: () => import('../pages/login/Login.vue'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/loading',
    component: () => import('@/pages/login/Loading.vue'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/loading',
    
  },


  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: 'System',
    meta: { title: '系统', icon: 'el-icon-s-platform', role: 'admin' },
    children: [
      {
        path: 'user',
        name: 'user',
        component: () => import('@/pages/system/User'),
        meta: { title: '用户管理', icon: 'el-icon-user', role: 'admin' }
      },
      {
        path: 'role',
        name: 'role',
        component: () => import('@/pages/system/Role'),
        meta: { title: '角色管理', icon: 'el-icon-user-solid', role: 'admin' }
      },
      {
        path: 'address',
        name: 'address',
        component: () => import('@/pages/system/Address'),
        meta: { title: '地址管理', icon: 'el-icon-location-information', role: 'admin' }
      },

    ]
  },

  {
    path: '/framework',
    component: Layout,
    redirect: '/framework/category',
    name: 'Framework',
    meta: { title: '基础架构', icon: 'el-icon-office-building', role: 'admin' },
    children: [
      {
        path: 'category',
        name: 'category',
        component: () => import('@/pages/framework/Category'),
        meta: { title: '栏目管理', icon: 'el-icon-guide', role: 'admin' }
      },
      {
        path: 'product',
        name: 'product',
        component: () => import('@/pages/framework/Product'),
        meta: { title: '产品管理', icon: 'el-icon-goods', role: 'admin' }
      },
    ]
  },

  {
    path: '/order',
    component: Layout,
    meta: {role: 'admin' },
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/pages/order/List'),
        meta: { title: '订单管理', icon: 'el-icon-shopping-bag-1', role: 'admin' }
      },
    ]
  },

  {
    path: '/check',
    component: Layout,
    redirect: '/check/employee',
    name: 'Check',
    meta: {
      title: '审核大厅', icon: 'el-icon-office-building', role: 'admin'
    },
    children: [
      {
        path: 'list',
        name: 'list',
        component: () => import('@/pages/check/List'),
        meta: { title: '账户列表', icon: 'el-icon-bank-card', role: 'admin' }
      },
      {
        path: 'employee',
        name: 'employee',
        component: () => import('@/pages/check/Employee'),
        meta: { title: '员工审核', icon: 'el-icon-s-check', role: 'admin' }
      },

      {
        path: 'account',
        name: 'account',
        component: () => import('@/pages/check/Account'),
        meta: { title: '提现审核', icon: 'el-icon-wallet', role: 'admin' }
      },
      {
        path: 'refund',
        name: 'refund',
        component: () => import('@/pages/check/Refund'),
        meta: { title: '退款审核', icon: 'el-icon-s-finance', role: 'admin' }
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    meta: { role: '顾客' },
    children: [
      {
        path: 'shopping',
        name: 'shopping',
        component: () => import('@/pages/user/Shopping'),
        meta: { title: '下单', icon: 'el-icon-goods', role: '顾客' }
      }
    ]
  },

  {
    path: '/user/orders',
    component: Layout,
    meta: { role: '顾客' },
    children: [
      
      {
        path: 'orders',
        name: 'orders',
        component: () => import('@/pages/user/Orders'),
        meta: { title: '我的订单信息', icon: 'el-icon-shopping-cart-1', role: '顾客' }
      }
    ]
  },

  {
    path: '/user/address',
    component: Layout,
    meta: { role: '顾客' },
    children: [
      
      {
        path: 'orders',
        name: 'orders',
        component: () => import('@/pages/user/Address'),
        meta: { title: '我的地址信息', icon: 'el-icon-map-location', role: '顾客' }
      }
    ]
  },

  {
    path: '/employee',
    component: Layout,
    meta: { role: '员工' },
    children: [
      {
        path: 'orders',
        name: 'orders',
        component: () => import('@/pages/employee/Orders'),
        meta: { title: '订单信息', icon: 'el-icon-shopping-cart-1', role: '员工' }
      }
    ]
  },

  {
    path: '/employee/withdrawal',
    component: Layout,
    meta: { role: '员工' },
    children: [
      {
        path: 'Withdrawal',
        name: 'Withdrawal',
        component: () => import('@/pages/employee/Withdrawal'),
        meta: { title: '个人信息', icon: 'el-icon-user-solid', role: '员工' }
      }
    ]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
