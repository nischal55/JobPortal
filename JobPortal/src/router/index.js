import UserLogin from '@/pages/user-login.vue'
import { createRouter, createWebHistory } from 'vue-router'
const routes = [
    {
        path: '/user-login',
        name: 'user-login',
        component: UserLogin,
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

// Global navigation guard
router.beforeEach((to, from, next) => {

})

export default router