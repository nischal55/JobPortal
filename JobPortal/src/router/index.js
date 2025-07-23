import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/pages/dashboard.vue'
import UserLogin from '@/pages/UserLogin.vue'
import ApiService from '@/services/ApiService'
import JobSeeker from '@/pages/JobSeeker.vue'

const routes = [
  {
    path: '/userLogin',
    name: 'userLogin',
    component: UserLogin,
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: Dashboard,
    meta: { requiresAuth: true },
  },
  {
    path: '/jobseeker',
    name: 'jobseeker',
    component: JobSeeker,
    meta: { requiresAuth: true },
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      await ApiService.get('/users/check')
      next()
    } catch (err) {
      next({ name: 'userLogin' })
    }
  } else {
    next() 
  }
})

export default router
