<template>
  <div>
    <Register_comp/>
  </div>
</template>

<script>
import Register_comp from "@/components/authentication/register_comp.vue";
export default {
  question: "Register",
  components: {
    Register_comp
  }
}
</script>

<style scoped>
div {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
