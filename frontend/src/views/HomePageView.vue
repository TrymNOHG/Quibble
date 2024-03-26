<template>
  <div class="homepage-format">
    <div class="searchbar">
      <SearchInput
          v-model="searchInput"
          @update:modelValue="handleSearchInput"
      />
    </div>
    <div class="search_query">
      <One_quiz_rectangle
          v-for="(quiz, index) in quizzes"
          :key="index"
          :quiz="quiz"
      />
    </div>
    <div
        id="inf_scroll"
    />
  </div>
</template>

<script setup>
import SearchInput from "@/components/searchbar.vue";
import {onMounted, ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";
import {useQuizStore} from "@/stores/counter.js";

const searchInput =  ref('');
const quizList = ref([]);

/*
  beforeMount() {
   useQuizStore().setAllQuizzes();
   quizzes.value = useQuizStore().allQuiz;a
},
   */

async function getNextQuiz() {
  window.onscroll = () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
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
      quizzes.value = [...quizzes.value, ...newQuizzes];
    }
  };
}

async function handleSearchInput(value) {
  if (value.length >= 3) {
    console.log('Search input changed:', value);
    quizzes.value = await useQuizStore().searchQuizzes(value);
  } else {
    await useQuizStore().setAllQuizzes();
    quizzes.value = useQuizStore().allQuiz;
  }
}

onMounted(() => {
  getNextQuiz();
});

let quizzes = ref([
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
