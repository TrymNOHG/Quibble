<template>
  <div class="homepage-format">
    <div class="searchbar">

    </div>
    <div class="search_query">
      <One_quiz_rectangle
          v-for="(quiz, index) in displayedQuizzes"
          :key="index"
          :quiz="quiz"
          @setCurrentQuiz=""
      />
    </div>
    <div id="inf_scroll"/>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";
import {useQuizStore} from "@/stores/counter.js";

let displayedQuizzes = ref([]);
const store = useQuizStore();
const quizStore = useQuizStore();
const categories = quizStore.category_list;


onMounted(async () => {
  displayedQuizzes.value = await store.loadMyQuizzes();
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
