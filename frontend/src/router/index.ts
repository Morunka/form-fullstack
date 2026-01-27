import { createRouter, createWebHistory } from "vue-router"

// @ts-ignore
// @ts-ignore
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'Home',
            component: () => import('../views/Home.vue'),
            meta: {
                title: 'Origami users manager',
                requiresAuth: false
            }
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import ('../views/Login.vue'),
            meta: {
                title: 'Login',
                requiresAuth: false
            }
        },
        {
            path: '/register',
            name: 'Register',
            component: () => import ('../views/Register.vue'),
            meta: {
                title: 'Register',
                requiresAuth: false
            }
        },
        {
            path: '/manager',
            name: 'Manager',
            component: () => import ('../views/Manager.vue'),
            meta: {
                title: 'Manager',
                requiresAuth: true
            }
        }
    ]
/*    scrollBehavior(to, from, savedPosition) {
        if (to.hash) {
            return {
                el: to.hash,
                behavior: 'smooth'
            }
        }

        return {top: 0, behavior: 'smooth'}
    }*/
})

/* // Global hooks
router.beforeEach((to, from, next) => {
    // Maybe add authorization logic here
    document.title = to.meta.title || 'My website'
    next()
}) */

export default router