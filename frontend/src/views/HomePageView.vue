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
let page = ref(-1);



onBeforeMount(() => {
  //displayedQuizzes = useQuizStore().loadQuizzes();
  //TODO: get quizzes from backend
  displayedQuizzes.value = test_quizzes.value;
})

async function getNextQuiz() {
  window.onscroll = async () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      try {
        const s = (searchInput.value === '') ? null : searchInput.value;
        const d = (difficulty_selected.value === '') ? null : difficulty_selected.value;
        page++
        displayedQuizzes = useQuizStore().loadQuizzes(s, d, page, 10);
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
    displayedQuizzes = useQuizStore().loadQuizzes(s, d, 0, 10);
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
    displayedQuizzes = useQuizStore().loadQuizzes(s, d, page, 10);
  } catch (e) {
    page--
    console.error(e);
  }
}

onMounted(() => {
  getNextQuiz();
});

let test_quizzes = ref([
  {
    quizId: 2,
    quizName: "Demo Quiz",
    quizDifficulty: "Medium",
    quizDescription: "This is a demo quiz for testing purposes",
    admin_id: useUserStore().user.userId,
    feedbacks: new Set(["Feedback 1", "Feedback 2", "Feedback 3"]),
    collaborators: new Set(["Collaborator 1", "Collaborator 2", "Collaborator 3"]),
    categories: new Set(["Category 1", "Category 2", "Category 3"]),
    questions: [
      {
        id: 1,
        question: "What is 2 + 2?",
        answer: "4",
        type: "multiple_choice",
        choices: [
          { alternative: "2", isCorrect: false },
          { alternative: "3", isCorrect: false },
          { alternative: "4", isCorrect: true },
          { alternative: "5", isCorrect: false }
        ]
      },
      {
        id: 2,
        question: "What is the capital of France?",
        answer: "Paris",
        type: "multiple_choice",
        choices: [
          { alternative: "London", isCorrect: false },
          { alternative: "Paris", isCorrect: true },
          { alternative: "Berlin", isCorrect: false },
          { alternative: "Dublin", isCorrect: false }
        ]
      },
      {
        id: 3,
        question: "Is the earth flat?",
        answer: "false",
        type: "true_false",
        choices: [
          { alternative: "true", isCorrect: true },
          { alternative: "false", isCorrect: false },
        ]
      }
    ],
    keywords: new Set(["Keyword 1", "Keyword 2", "Keyword 3"]),
    Image: "https://via.placeholder.com/150"
  }
]);

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
