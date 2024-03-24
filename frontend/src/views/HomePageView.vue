<template>
  <div class="homepage-format">
    <div class="searchbar">
      <SearchInput
          v-model="searchInput"
          @update:modelValue="handleSearchInput"
      />
    </div>
  </div>
</template>

<script setup>
import SearchInput from "@/components/searchbar.vue";
import {ref} from 'vue';

const searchInput = ref('');

function handleSearchInput(value) {
  console.log('Search input changed:', value);
}
</script>

<style scoped>
.homepage-format {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 2%;
}

.searchbar {
  width: 100%;
  align-self: center;
}
</style>
