<template>
  <div class="page_format">
    <quiz_info_comp class="info" :quiz="quiz"/>
    <quiz_questions_list_comp class="btn_questionList"/>
  </div>
</template>

<script setup>

import {useQuizStore} from "@/stores/counter.js";
import Quiz_info_comp from "@/components/quiz_info_comp.vue";
import Quiz_questions_list_comp from "@/components/quiz_quiestions_list_comp.vue";

const store = useQuizStore();
const quiz = store.currentQuiz;


</script>

<style scoped>

.page_format {
  display: grid;
  grid-template-columns: 25% 75%;
  min-height: 600px;
}

.info {
  background-color: #fff;
  border-width: 0 1px 0 0;
  padding: 20px;
  border-style: solid;
  border-color: #ccc;
  height: 100%;

  display: flex;
  flex-direction: column;
  align-content: center;
}

.btn_questionList {
  height: 100%;
  width: 100%;
  background: white;
}

</style>