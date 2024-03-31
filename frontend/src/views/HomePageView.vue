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
import {useQuizStore} from "@/stores/counter.js";

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
    QuizId: null,
    Name: "TemplateQuiz",
    Difficulty: "Easy",
    Description: "Template quiz, change the quiz as wanted",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
  {
    QuizId: null,
    Name: "AnotherQuiz",
    Difficulty: "Medium",
    Description: "Another quiz description",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
  {
    QuizId: null,
    Name: "YetAnotherQuiz",
    Difficulty: "Hard",
    Description: "Yet another quiz description",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
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
