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
    <div class="search-filter-icon">
      <font-awesome-icon
          class="search-icon"
          icon="fa-solid fa-magnifying-glass"
          @click="toggleSearchBar"
      />
    </div>
    <div v-if="showFilter" class="dropdown_phone">
      <font-awesome-icon class="filter_bar" icon="fa-bars" @click="toggleDropdownPhone"/>
      <div class="dropdown" @click="toggleDropdown" v-if="!dropdownPhone">
        <div class="dropdown-content" v-show="true">
          <div @click="selectDifficulty('')">All Difficulties</div>
          <div @click="selectDifficulty('Easy')">Easy</div>
          <div @click="selectDifficulty('Medium')">Medium</div>
          <div @click="selectDifficulty('Hard')">Hard</div>
        </div>
      </div>
    </div>
    <div class="dropdown" @click="toggleDropdown" v-if="!showFilter">
      {{ selectedDifficulty ? selectedDifficulty : 'All Difficulties' }}
      <div class="dropdown-content" v-show="dropdownOpen">
        <div @click="selectDifficulty('')">All Difficulties</div>
        <div @click="selectDifficulty('Easy')">Easy</div>
        <div @click="selectDifficulty('Medium')">Medium</div>
        <div @click="selectDifficulty('Hard')">Hard</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref} from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const searchInput = ref("");
const showSearchBar = ref(true);
const showFilter = ref(true);
const selectedDifficulty = ref('');
const dropdownOpen = ref(false);
const dropdownPhone = ref(false);

const { emit } = getCurrentInstance();

function toggleDropdownPhone() {
  dropdownPhone.value = !dropdownPhone.value;
  console.log(dropdownPhone)
}
function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value;
}
function selectDifficulty(difficulty) {
  selectedDifficulty.value = difficulty;
  emit('difficultySelected', difficulty);
  dropdownOpen.value = false;
}

function toggleSearchBar() {
  showSearchBar.value = !showSearchBar.value;
}

onMounted(() => {
  showSearchBar.value = !(window.innerWidth <= 428);
  showFilter.value = (window.innerWidth <= 428);
});

</script>

<style scoped>

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

  .filter_bar {
    display: flex;
    cursor: pointer;
    margin-top: 30px;
    scale: 2.2;
    margin-left: 15px;
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
}
</style>
