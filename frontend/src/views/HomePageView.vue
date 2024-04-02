<template>
  <div class="homepage-format">
    <div class="searchbar">
      <SearchInput
          v-model="searchInput"
          @update:modelValue="handleSearchInput"
          @difficultySelected = "handleDifficulty"
      />
    </div>
    <div class="search_query">
      <One_quiz_rectangle
          v-for="(quiz, index) in displayedQuizzes"
          :key="index"
          :quiz="quiz"
      />
    </div>
    <div id="inf_scroll"/>
  </div>
</template>

<script setup>
import SearchInput from "@/components/searchbar.vue";
import {onBeforeMount, onMounted, ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";
import {useQuizStore, useUserStore} from "@/stores/counter.js";

const searchInput =  ref('');
let displayedQuizzes = ref([]);
let difficulty_selected = ref('');
let page = ref(0);



onMounted(async () => {
  await loadQuizzes();
});

async function loadQuizzes() {
  try {
    const s = (searchInput.value === '') ? null : searchInput.value;
    const d = (difficulty_selected.value === '') ? null : difficulty_selected.value;
    displayedQuizzes.value = await useQuizStore().loadQuizzes(page.value, 10);
  } catch (e) {
    console.error(e);
  }
}

async function getNextQuiz() {
  window.onscroll = async () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      try {
        const s = (searchInput.value === '') ? null : searchInput.value;
        const d = (difficulty_selected.value === '') ? null : difficulty_selected.value;
        page++
        displayedQuizzes = useQuizStore().loadQuizzes(page);
      } catch (e) {
        page--
        console.error(e);
      }
    }
  }
}

async function handleDifficulty(difficulty) {
  difficulty_selected = difficulty
  displayedQuizzes = [];
  page = -1;
  try {
    const s = (searchInput.value === '') ? null : searchInput.value;
    const d = (difficulty_selected.value === '') ? null : difficulty_selected.value;
    page++
    displayedQuizzes = useQuizStore().loadQuizzes(page);
  } catch (e) {
    page--
    console.error(e);
  }
}

async function handleSearchInput() {
  try {
    const s = (searchInput.value === '') ? null : searchInput.value;
    const d = (difficulty_selected.value === '') ? null : difficulty_selected.value;
    page++
    displayedQuizzes = useQuizStore().loadQuizzes(page);
  } catch (e) {
    page--
    console.error(e);
  }
}

onMounted(() => {
  getNextQuiz();
});

</script>

<style scoped>
.homepage-format {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 1%;
}

.searchbar {
  width: 100%;
  align-self: center;
}

.search_query {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  z-index: 0;
}

@media only screen and (max-width: 428px) {
  .search_query {
    display: grid;
    grid-template-columns: 1fr;
    margin: 2%;
    z-index: 0;
  }

  .searchbar {
    width: 100%;
    align-self: center;
  }
}

</style>
