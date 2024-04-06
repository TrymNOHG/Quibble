import { createRouter, createWebHistory } from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import Quiz_comp from "@/components/QuizPlaing/Quiz_comp.vue";
import HomePageView from "@/views/HomePageView.vue";
import CurrentQuizView from "@/views/CurrentQuizView.vue";
import {useUserStore} from "@/stores/counter.js";
import ProfileView from "@/views/Profile/PrivateProfileView.vue";
import multiplayerHostView from "@/views/QuizPlaying/MultiplayerHostView.vue";
import CreateQuizView from "@/views/CreateQuizView.vue";
import multiplayerPlayerView from "@/views/QuizPlaying/MultiplayerPlayerView.vue";
import MyQuizView from "@/views/MyQuizView.vue";


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
          path: '/home',
          name: 'homepage',
          component: HomePageView,
          meta: { requiresAuth: false }
      },
      {
          path: '/myquiz',
          name: 'myquiz',
          component: MyQuizView,
          meta: { requiresAuth: true }
      },
      {
          path: '/quiz/current',
          name: 'CurrentQuiz',
          component: CurrentQuizView
      },
      {
          path: '/login',
          name: 'Login',
          component: LoginView,
          meta: { requiresAuth: false }
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
      },
      {
          path: '/create',
          name: 'CreateQuiz',
          component: CreateQuizView,
          meta: { requiresAuth: true }
      },
        {
          path: '/quiz/multiplayer',
          name: 'multiplayer',
          component: multiplayerHostView,
          meta: { requiresAuth: true }
        },
      {
          path: '/quiz/game/:gameId',
          name: 'GameClient',
          component: multiplayerPlayerView,
          props: true, // Allows the route parameter (gameId) to be passed as a prop to the component
          meta: { requiresAuth: false } // Adjust based on whether you want this route to require authentication
      },
  ]
})

router.beforeEach((to, from, next) => {
    const store = useUserStore();
    const isAuthenticated = store.isAuth

    const notRequiresAuth = to.matched.some(record => record.meta.requiresAuth === false);
    if (to.matched.length === 0) {
        next({ path: '/' });
    }
    else if (notRequiresAuth) {
        if (['/register', '/login'].includes(to.path) && isAuthenticated) {
          next({ path: '/home' });
        } else {
            next();
        }
    } else {
      if (!isAuthenticated) {
          next({path: '/login'});
      }
    }
    next();
});


export default router
