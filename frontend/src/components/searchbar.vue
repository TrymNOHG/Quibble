<script setup>

</script>

<template>
  <div class="searchbar">
    <input
        type="search"
        required
        v-model.trim="search_word"
        name="searchbar"
        class="input-field"
        aria-labelledby="searchLabel"
        placeholder="Search ..."
    />
    <font-awesome-icon icon="fa-solid fa-magnifying-glass"/>
  </div>
</template>

<style scoped>

.searchbar{
  width: 100%;
}

</style>