import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

// When auth fixed

/*
router.beforeEach((to, from, next) => {
  const userStore = useLoggedInStore();
  const isAuthenticated = userStore.isLoggedIn;

  const fridgeStore = useFridgeStore();
  const hasCurrentFridge = fridgeStore.hasCurrentFridge;

  const notRequiresAuth = to.matched.some(record => record.meta.requiresAuth === false);
  const requiresCurrentFridge = to.matched.some(record => record.meta.requiresCurrentFridge === true);


  if (to.matched.length === 0) {
    next({ path: '/' });
  }
  else if (notRequiresAuth) {
    if (['/', '/register', '/login'].includes(to.path) && isAuthenticated) {
      next({ path: '/home' });
    } else {
      next();
    }
  } else {
    if (!isAuthenticated) {
      next({ path: '/' });
    } else {
      if (requiresCurrentFridge && !hasCurrentFridge) {
        next({ path: '/home' });
      } else {
        next();
      }
    }
  }
});
 */


export default router
