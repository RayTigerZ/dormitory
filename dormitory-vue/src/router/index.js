import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from '../views/Index.vue';
import store from '../store';
import { initMenu } from '../utils/utils';
Vue.use(VueRouter);

/**
 * 重写路由的push方法
 */
//const routerPush = VueRouter.prototype.push;
// VueRouter.prototype.push = function push(location) {
//     return routerPush.call(this, location).catch(error => error);
// };

const routes = [
	{
		path: '/index',
		name: 'Index',
		alias: ['/'],
		component: Index
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import('../views/Login.vue')
	},
	{
		path: '/about',
		name: 'About',
		component: () => import('../views/About.vue') //懒加载组件，访问时才加载组件
	}
];

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
});

router.beforeEach((to, from, next) => {
	//要进入的页面是登录界面，直接进入登录页面
	//debugger
	if (to.name == 'Login') {
		next();
		return;
	}

	var token = store.state.token;

	if (token == '') {
		if (to.meta.requireAuth || to.name == null) {
			next({ path: '/login', query: { redirect: to.path } });
		} else {
			next();
		}
	} else {
		initMenu(router, store);
		next();
	}
	return;
});

export default router;
