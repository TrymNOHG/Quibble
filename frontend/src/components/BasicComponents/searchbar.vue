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
        :placeholder="$t('placeholders.SEARCH')"
        @input="$emit('update:modelValue', $event.target.value)"
    />
    <div class="search-filter-icon">
      <font-awesome-icon
          class="search-icon"
          icon="fa-solid fa-magnifying-glass"
          @click="toggleSearchBar"
      />
    </div>
    <div class="dropdown">
      <font-awesome-icon class="filter_bar" icon="fa-bars" @click="toggleDropdown"/>
      <div class="dropdown-content" v-show="dropdownOpen">
        <div class="options-row">
          <div v-for="difficulty in difficulties" :key="difficulty">
            <input
                type="checkbox"
                :id="difficulty"
                :value="difficulty"
                v-model="selectedDifficulties"
            />
            <label :for="difficulty">{{ $t(`dropdown_options.${difficulty.toUpperCase()}`) }}</label>
          </div>
        </div>
        <div class="options-row">
          <div v-for="category in categories" :key="category">
            <input
                type="checkbox"
                :id="category"
                :value="category"
                v-model="selectedCategories"
            />
            <label :for="category">{{ category }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance, onMounted, ref, watchEffect } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const showSearchBar = ref(true);
const selectedDifficulties = ref([]);
const selectedCategories = ref([]);
const dropdownOpen = ref(false);
const difficulties = ref(["Easy", "Medium", "Hard"]);
const categories = ref(["Category1", "Category2", "Category3", "Category4"]);

const { emit } = getCurrentInstance();

function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value;
}

function toggleSearchBar() {
  showSearchBar.value = !showSearchBar.value;
}

onMounted(() => {
  showSearchBar.value = !(window.innerWidth <= 428);
});

watchEffect(() => {
  console.log(selectedDifficulties.value);
  console.log(selectedCategories.value);
});

const rows = ref([]);
rows.value.push({ difficulties: difficulties.value });
rows.value.push({ categories: categories.value });
</script>

<style scoped>
.options-row {
  display: flex;
  justify-content: space-between;
}

.dropdown {
  position: relative;
  display: inline-block;
  cursor: pointer;
  align-self: center;
  justify-self: center;
  width: 95px;
  height: 25px;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content div {
  padding: 12px 16px;
  cursor: pointer;
}

.dropdown-content div:hover {
  background-color: #f1f1f1;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.search-filter-icon {
  margin-left: 5%;
  scale: 1.8;
  justify-self: center;
  align-self: center;
}

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

.filter_bar {
  scale: 1.5;
}

@media only screen and (max-width: 428px) {
  .search-container {
    position: fixed;
    width: 80%;
    display: flex;
    z-index: 1000;
    justify-content: center;
    align-items: center;
    margin: 2% 0;
  }

  .input-field {
    height: 45px;
    border-radius: 10px;
    width: 80%;
  }

  .search-icon {
    display: flex;
    cursor: pointer;
    margin-right: 15px;
    margin-left: 50px;
    margin-top: 20px;
    scale: 1.5;
  }

  .search-filter-icon {
    display: flex;
    cursor: pointer;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
    scale: 2;
    margin-left: auto;
    margin-right: auto;
    position: fixed;
    right: 35px;
    top: 5%;
  }

  .dropdown {
    position: absolute;
    display: inline-block;
    cursor: pointer;
    align-self: center;
    justify-self: center;
    width: 95px;
    height: 25px;
    z-index: 1000;
  }

  .dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
    z-index: 1;
  }

  .dropdown-content div {
    padding: 12px 16px;
    cursor: pointer;
  }

  .dropdown-content div:hover {
    background-color: #f1f1f1;
  }

  .dropdown:hover .dropdown-content {
    display: block;
  }

  .filter_bar {
    display: flex;
    cursor: pointer;
    margin-top: 10px;
    scale: 2.2;
    margin-left: auto;
    position: fixed;
    top: 8%;
    left: 25px;
  }

  .dropdown-content {
    display: flex;
    flex-direction: column;
    margin-top: 10px;
    margin-left: auto;
    position: fixed;
    top: 8%;
    left: 25px;
    width: 80%;
    padding: 20px;
    border-radius: 10px;
    background-color: #ffffff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    font-size: larger;
}

  .options-row {
    flex-direction: column;
  }
}

</style>
