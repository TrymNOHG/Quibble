import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import HomePageView from "@/views/HomePageView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
        path: '/',
        name: 'FrontPage',
        component: TheWelcomeComponent,
      },
      {
        path: '/login',
        name: 'register',
        component: RegisterView,
      },
      {
          path: '/home',
          name: 'homepage',
          component: HomePageView,
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
