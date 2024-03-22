<template>
  <div>
    <router-view v-if="isAuth" />
    <Login v-else/>
  </div>
</template>

<script>
import { useRoute, useRouter } from 'vue-router';
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import Login from "@/views/LoginView.vue";
import Register from "@/views/RegisterView.vue";

export default {
  components: {Register, Login, TheWelcomeComponent },

  setup() {
    const router = useRouter();
    const route = useRoute();
    const isAuth = false;

    if (!isAuth && route.path !== '/register') {
      router.push('/register');
    }

    return { isAuth };
  }
}
</script>

<style scoped>
</style>
