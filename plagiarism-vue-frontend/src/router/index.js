import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import {authService} from "../services/authService";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/detection',
    name: 'Detection',
    component: () => import(/* webpackChunkName: "detection" */ '../views/Detection.vue')
  },
  {
    path: '/api/docs',
    name: 'ApiDocs',
    component: () => import(/* webpackChunkName: "api_docs" */ '../views/ApiDocs.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import(/* webpackChunkName: "register" */ '../views/Register.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import(/* webpackChunkName: "register" */ '../views/Profile.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: "/logout",
    name: "logout",
    redirect: to => {
      return '/login'
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  // linkActiveClass: "active", // active class for non-exact links.
  linkExactActiveClass: "active" // active class for *exact* links.
})

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
  const publicPages = ['/login', '/', '/detection', '/about', '/register', '/logout'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = authService.getUser();

  if (authRequired && !loggedIn) {
    return next('/login');
  }

  next();
})

export default router
