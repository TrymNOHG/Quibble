<template>
  <div>
    <public-profile-component/>
  </div>

</template>

<script>
import publicProfileComponent from "@/components/Profile/PublicProfileComponent.vue";
export default {
  name: "ProfileView",
  components: {publicProfileComponent},
}
</script>

<style scoped>


</style>