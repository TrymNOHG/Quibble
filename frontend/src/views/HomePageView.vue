<template>
  <div class="homepage-format">
    <div class="searchbar">
      <searchbar

      />
    </div>

    <div class="quiz_view">
      <recommended

      />
    </div>
  </div>
</template>


<script setup>

import Searchbar from "@/components/searchbar.vue";
import recommended from "@/components/recommended_comp.vue";
</script>


<style scoped>

.homepage-format {
  display: flex;
  flex-direction: row;
  justify-content: center;
  justify-items: center;
}

</style>