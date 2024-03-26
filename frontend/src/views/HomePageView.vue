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
const recommendedQuizzes = ref([])
let displayedQuizzes = ref([]);

onBeforeMount(() => {
  useQuizStore().setAllQuizzes();
  recommendedQuizzes.value = test_quizzes.value;
  displayedQuizzes.value = test_quizzes.value;
})

async function getNextQuiz() {
  window.onscroll = () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      /*
      const newQuizzes = useQuizStore().getMoreQuizzes();
      quizzes.value = [...newQuizzes]
       */
      const newQuizzes = [
        {
          name: "New Quiz 1",
          difficulty: "Hard",
          description: "Test your knowledge with New Quiz 1",
          image: "https://via.placeholder.com/150",
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
      ];
      displayedQuizzes.value = [...displayedQuizzes.value, ...newQuizzes];
    }
  };
}

async function handleDifficulty(difficulty) {
  if (difficulty === "") {
    displayedQuizzes.value = recommendedQuizzes.value;
  }
  else {
    const filtered = recommendedQuizzes.value.filter(quiz => quiz.difficulty === difficulty);
    displayedQuizzes.value = filtered;
  }
}

async function handleSearchInput(searchword) {
  if (searchword.length >= 3) {
    //displayedQuizzes.value = await useQuizStore().searchQuizzes(searchword);
    let newQuizzelist = [{
          name: "Quiz 1",
          difficulty: "Easy",
          description: "Test your knowledge with Quiz 1",
          image: "https://via.placeholder.com/150",
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
        {
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        }];
    displayedQuizzes.value = newQuizzelist;
  } else {
    let newQuizzelist = recommendedQuizzes.value;
    displayedQuizzes.value = newQuizzelist;
  }
}

onMounted(() => {
  getNextQuiz();
});

let test_quizzes = ref([
  {
    name: "Quiz 1",
    difficulty: "Easy",
    description: "Test your knowledge with Quiz 1",
    image: "https://via.placeholder.com/150",
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    name: "Quiz 2",
    difficulty: "Hard",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
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
