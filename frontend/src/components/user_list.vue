<template>
  <div>
    <ul>
      <li v-for="(user, index) in users" :key="index">
        {{ user.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    users: {
      type: Array,
      default: () => []
    }
  }
};
</script>

<style scoped>
</style>
