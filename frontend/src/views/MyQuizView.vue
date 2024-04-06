<template>
  <div class="homepage-format">
    <div class="searchbar">
      <SearchInput
          v-model="searchInput"
          @update:modelValue="handleSearchInput"
          @difficultySelected="handleDifficulty"
          @categorySelected="handleCategory"
          :categories="categories"
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
import SearchInput from "@/components/BasicComponents/searchbar.vue";
import {onMounted, ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";
import {useQuizStore} from "@/stores/counter.js";

const searchInput = ref('');
let displayedQuizzes = ref([]);
let difficulty_selected = ref([]);
let category_selected = ref([]);
let page = ref(0);
const store = useQuizStore();
const quizStore = useQuizStore();
const categories = quizStore.category_list;


onMounted(async () => {
  await store.loadCategories();
  await loadQuizzes();
});

onMounted(() => {
  getNextQuiz();
});

async function handleDifficulty(difficulty) {
  difficulty_selected.value = difficulty;
  page.value = 0;
  await loadQuizzes();
}

async function handleCategory(category) {
  category_selected.value = category;
  page.value = 0;
  await loadQuizzes();
}

async function loadQuizzes() {
  try {
    const s = (searchInput.value === '') ? null : searchInput.value;
    const d = (difficulty_selected.value === []) ? null : difficulty_selected.value;
    const c = (category_selected.value === []) ? null : category_selected.value;

    const quizFilterDTO = {
      "name": s,
      "difficulties": d,
      "categories": c,
      "pageSize": 12,
      "pageNumber": page.value
    }

    displayedQuizzes.value = await useQuizStore().loadQuizzes(quizFilterDTO);
  } catch (e) {
    console.error(e);
  }
}

async function getNextQuiz() {
  window.onscroll = async () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      try {
        page.value++;
        const s = (searchInput.value === '') ? null : searchInput.value;
        const d = (difficulty_selected.value === []) ? null : difficulty_selected.value;
        const c = (category_selected.value === []) ? null : category_selected.value;

        const quizFilterDTO = {
          "name": s,
          "difficulties": d,
          "categories": c,
          "pageSize": 12,
          "pageNumber": page.value
        }

        displayedQuizzes.value = displayedQuizzes.value.concat(await useQuizStore().loadQuizzes(quizFilterDTO));
      } catch (e) {
        page.value--;
        console.error(e);
      }
    }
  }
}

async function handleSearchInput() {
  console.log(searchInput.value)
  try {
    displayedQuizzes.value = loadQuizzes().value;
  } catch (e) {
    console.error(e);
  }
}

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
