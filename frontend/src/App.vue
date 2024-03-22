<template>
  <div>
    <router-view v-if="isAuth" />
    <Login v-else />
  </div>
</template>

<script>
import { useRoute, useRouter } from 'vue-router';
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import Login from "@/views/LoginView.vue";

export default {
  components: {Login, TheWelcomeComponent },

  setup() {
    const router = useRouter();
    const route = useRoute();
    const isAuth = false;

    if (!isAuth && route.path !== '/login') {
      router.push('/login');
    }

    return { isAuth };
  }
}
</script>

<style scoped>
</style>
