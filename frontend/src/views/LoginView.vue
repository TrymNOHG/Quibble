<template>
  <div>
    <LoginComponent/>
  </div>
</template>

<script>
import LoginComponent from "@/components/login_comp.vue";

export default {
  name: "login",
  components: {
    LoginComponent
  }
}
</script>

<style scoped>
div {
  margin-top: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
