<template>
  <div class="searchbar">
    <input
        type="search"
        required
        v-model.trim="searchInput"
        name="searchbar"
        class="input-field"
        aria-labelledby="searchLabel"
        placeholder="Search ..."
        @input="$emit('update:modelValue', $event.target.value)"
    />
    <!--
    <font-awesome-icon
        class="search-icon"
        icon="fa-solid fa-magnifying-glass"
        @click="handleSearch"
    />
    -->
  </div>
</template>

<script setup>
import { ref } from "vue";

const searchInput = ref("");

function handleSearch() {
  console.log('Search input:', searchInput.value);
  this.$emit("search", searchInput.value);
}
</script>

<style scoped>
.searchbar {
  display: flex;
  align-items: center;
  justify-content: center;
}

.input-field {
  height: 45px;
  border-radius: 10px;
  width: 25%;
}

.search-icon {
  margin-left: -35px;
  cursor: pointer;
}
</style>
