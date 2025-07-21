import Dashboard from '@/pages/dashboard.vue';
import UserLogin from '@/pages/UserLogin.vue'
import { createRouter, createWebHistory } from 'vue-router'
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
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router;