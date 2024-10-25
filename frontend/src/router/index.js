import { createRouter, createWebHistory } from 'vue-router'
import PageHome from '@/views/PageHome.vue'
import LoginPage from '@/components/LoginPage.vue'

const routes = [
  {
    path: '/',               // 기본 경로로 로그인 페이지 설정
    name: 'LoginPage',
    component: LoginPage,
  },

  {
    path: '/PageHome',
    name: 'PageHome',
    component: PageHome
  },
  {
    path: '/PagePgmBase',
    name: 'PagePgmBase',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/PagePgmBase.vue')
  },
  {
    path: '/PagePgmDtl',
    name: 'PagePgmDtl',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/PagePgmDtl.vue')
  },
  {
    path: '/ClientReturn',
    name: 'ClientReturn',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/ClientReturn.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router