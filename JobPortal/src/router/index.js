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
import JobDetailView from '@/pages/JobDetailView.vue'
import MyApplication from '@/pages/MyApplication.vue'
import AvailableJobList from '@/pages/AvailableJobList.vue'
import UpdateResume from '@/pages/UpdateResume.vue'
import JobApplicantsList from '@/pages/JobApplicantsList.vue'
import JobList from '@/pages/JobList.vue'
import JobProviderList from '@/pages/JobProviderList.vue'
import ResumeTemplate from '@/pages/ResumeTemplate.vue'

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
    path: '/',
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
    component : JobCreateForm,
    meta: { requiresAuth: true },
  },
  {
    path: '/jobDetail/:id',
    name: 'jobDetail',
    component : JobDetailView
  },
  {
    path: '/myApplications',
    name: 'myApplications',
    component : MyApplication,
    meta: { requiresAuth: true },
  },
  {
    path: '/allJobs',
    name: 'allJobs',
    component : AvailableJobList
  },
  {
    path: '/updateResume',
    name: 'updateResume',
    component : UpdateResume,
    meta: { requiresAuth: true },
  },
  {
    path: '/applicants',
    name: 'applicants',
    component : JobApplicantsList,
    meta: { requiresAuth: true },
  },
  {
    path: '/jobList',
    name: 'jobList',
    component : JobList,
    meta: { requiresAuth: true },
  },
  {
    path: '/jobProviderList',
    name: 'jobProviderList',
    component : JobProviderList,
    meta: { requiresAuth: true },
  },
  {
    path: '/viewResume',
    name: 'viewResume',
    component : ResumeTemplate,
    meta: { requiresAuth: true },
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

const userId = localStorage.getItem('user_id')

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
        if (!userId) {
      return next({ name: 'userLogin' })
    }
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
