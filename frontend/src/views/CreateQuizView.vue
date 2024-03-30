<template>
  <div class="page_format">
    <quiz_create_info class="info" :template_quiz="quiz" :template_tags="tags"/>
    <quiz_create_question class="btn_questionList" :template_quiz="quiz"/>
  </div>
</template>

<script setup>

import {useQuizCreateStore} from "@/stores/counter.js";
import Quiz_create_info from "@/components/create_quiz/quiz_create_info.vue";
import Quiz_create_question from "@/components/create_quiz/quiz_create_question.vue";

const store = useQuizCreateStore();
const quiz = store.templateQuiz;
const tags = store.template_tags;


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

@media only screen and (max-width: 428px) {
  .page_format {
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    min-height: 900px;
    margin-bottom: 25%;
  }

  .info{
    margin-bottom: 10%;
  }
}

</style>