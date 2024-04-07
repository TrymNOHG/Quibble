<template>
  <div class="page_format">
    <quiz_info_comp
        class="info"
        :quiz="quiz"
        :isAuthor="isAuthor"
        :isEditor="isEditor"
        :quizAuthors="quizAuthors"
        @deleteAuthor="deleteAuthor"
        @saveEdit="saveEdit"
        @addAuthor="addAuthor"
    />

    <quiz_quiestions_list_comp
        class="btn_questionList"
        :isAuthor="isAuthor"
        :isEditor="isEditor"
    />
  </div>
</template>

<script setup>

import {useQuizStore, useUserStore} from "@/stores/counter.js";
import {onMounted, ref} from "vue";
import Quiz_quiestions_list_comp from "@/components/quiz_quiestions_list_comp.vue";
import Quiz_info_comp from "@/components/quiz_info_comp.vue";
import {getPictureFromID} from "@/services/ImageService.js";

const store = useQuizStore();
const quiz = store.currentQuiz;
const userStore = useUserStore();

let isAuthor = ref(true);
let isEditor = ref(true);
let quizAuthors = ref(store.currentQuiz.collaborators === null ? [] : store.currentQuiz.collaborators);
quizAuthors.value = quizAuthors.value.filter((author, index, self) =>
    index === self.findIndex(a => a.username === author.username)
);
quiz.collaborators = quizAuthors.value

onMounted( () => {
  isAuthor.value = store.isAdmin(store.currentQuiz.adminId)
  checkEditor();
});

const checkEditor = () => {
  isEditor.value = quizAuthors.value.some(author => author.userId === userStore.user.userId);
};

const deleteAuthor = (author) => {
  store.deleteAuth(author);
};

const addAuthor = async (author) => {
  await store.addAuthor(author);
  quizAuthors.value = await store.currentQuiz.collaborators;
  quizAuthors.value = quizAuthors.value.filter((author, index, self) =>
      index === self.findIndex(a => a.username === author.username)
  );
  quiz.collaborators = quizAuthors.value
};

const saveEdit = async (quizUpdateDTO) => {
  try {
    return await store.updateCurrentQuiz(quizUpdateDTO);
  } catch (error) {
    console.error('Error editing quiz:', error);
  }
};

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