import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../views/Index.vue'

Vue.use(VueRouter)

const routes = [{
        path: '/',
        redirect: '/index'
    },
    {
        path: '/index',
        name: 'index',
        component: Index
    },
    {
        path: '/about',
        name: 'about',
        component: () => import('../views/About.vue') //懒加载组件，访问时才加载组件
    },{
        path:'/manage',
        name:'manage',
        component:()=>import('../views/Manage.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router