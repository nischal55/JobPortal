import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/pages/dashboard.vue'
import UserLogin from '@/pages/UserLogin.vue'
import ApiService from '@/services/ApiService'
import JobSeeker from '@/pages/JobSeeker.vue'
import HomePage from '@/pages/HomePage.vue'
import JobSeekerCreate from '@/pages/JobSeekerCreate.vue'
import Registration from '@/pages/Registration.vue'
import JobProviderCreate from '@/pages/JobProviderCreate.vue'
import JobCreateForm from '@/pages/JobCreateForm.vue'

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
  {
    path: '/home',
    name: 'home',
    component: HomePage,
  },
  {
    path: '/jobseekerAccountCreation',
    name: 'jobseekerAccountCreation',
    component : JobSeekerCreate
  },
  {
    path: '/registration',
    name: 'registration',
    component : Registration
  },
  {
    path: '/jobProviderCreation',
    name: 'jobProviderCreation',
    component : JobProviderCreate
  },
  {
    path: '/jobCreateForm',
    name: 'jobCreateForm',
    component : JobCreateForm
  }

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
