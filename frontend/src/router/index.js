import { createRouter, createWebHistory } from 'vue-router'
import RegisterView from "@/views/RegisterView.vue";
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'front_page',
      component: TheWelcomeComponent,
      //meta: {requiresAuth: false}
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
      //meta: {requiresAuth: false}
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
      //meta: {requiresAuth: false}
    },
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
