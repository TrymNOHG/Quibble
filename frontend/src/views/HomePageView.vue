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
  </div>
</template>

<script setup>
import SearchInput from "@/components/searchbar.vue";
import {ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";

const searchInput = ref('');

function handleSearchInput(value) {
  console.log('Search input changed:', value);
}

const quizzes = ref([
  {
    name: "Quiz 1",
    difficulty: "Easy",
    description: "Test your knowledge with Quiz 1",
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
