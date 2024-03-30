import { createRouter, createWebHistory } from 'vue-router'
import RegisterView from "@/views/RegisterView.vue";
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import Quiz_comp from "@/components/QuizPlaing/Quiz_comp.vue";
import HomePageView from "@/views/HomePageView.vue";
import {useUserStore} from "@/stores/counter.js";
import LoginView from "@/views/LoginView.vue";
import ProfileView from "@/views/PrivateProfileView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
        path: '/',
        name: 'FrontPage',
        component: TheWelcomeComponent,
        meta: { requiresAuth: false }
      },
      {
        path: '/register',
        name: 'register',
        component: RegisterView,
        meta: { requiresAuth: false }
      },
      {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { requiresAuth: false }
      },
      {
          path: '/home',
          name: 'homepage',
          component: HomePageView,
          meta: { requiresAuth: true }
      },
        {
            path: '/quiz',
            name: 'quiz',
            component: Quiz_comp,
            meta: { requiresAuth: false }
        },
      {
          path: '/profile',
          name: 'profile',
          component: ProfileView,
          meta: { requiresAuth: true }
      }

  ]
})

// When auth fixed

/*
router.beforeEach((to, from, next) => {
    const store = useUserStore();
    const isAuthenticated = store.isAuth

  const notRequiresAuth = to.matched.some(record => record.meta.requiresAuth === false);

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
          next({path: '/'});
      }
  }
});
*/


export default router
