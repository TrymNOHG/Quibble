<template>
  <div class="search-container">
    <input
        v-show="showSearchBar"
        type="search"
        required
        v-model.trim="searchInput"
        name="searchbar"
        class="input-field"
        aria-labelledby="searchLabel"
        placeholder="Search ..."
        @input="$emit('update:modelValue', $event.target.value)"
    />
    <font-awesome-icon
        class="search-icon"
        icon="fa-solid fa-magnifying-glass"
        @click="toggleSearchBar"
    />
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";

const searchInput = ref("");
const showSearchBar = ref(true);

function toggleSearchBar() {
  showSearchBar.value = !showSearchBar.value;
}

onMounted(() => {
  showSearchBar.value = !(window.innerWidth <= 428);
});

</script>

<style scoped>

.search-container {
  display: flex;
  align-content: center;
  justify-content: center;
}

.input-field {
  height: 45px;
  border-radius: 10px;
  width: 25%;
}

.search-icon {
  display: none;
}

@media only screen and (max-width: 428px) {

  .search-container {
    position: fixed;
    width: 80%;
    display: flex;
    z-index: 1000;
    justify-content: flex-end;
    flex-wrap: nowrap;
    flex-direction: row;
    align-items: flex-start;
    margin: 2% 2% 2% 15%;

  }

  .input-field {
    height: 45px;
    border-radius: 10px;
    width: 150%;
  }

  .search-icon {
    display: flex;
    cursor: pointer;
    margin-right: 15px;
    margin-left: 10px;
    margin-top: 20px;
    scale: 2;
  }
}
</style>
