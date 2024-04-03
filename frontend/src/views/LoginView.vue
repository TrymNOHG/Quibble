<template>

  <div>
    <login_comp/>
  </div>

</template>

<script>


import login_comp from "@/components/login_comp.vue";

export default {
  question: "LoginView",
  components: {login_comp}
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