<template>
  <div class="page_format">
    <quiz_create_info
        class="info"
    />
    <quiz_create_question
        class="btn_questionList"
        @createQuiz="saveQuiz"
    />
  </div>
</template>

<script>

import {useQuizCreateStore, useQuizStore} from "@/stores/counter.js";
import {addCategories, addCategory} from "@/services/QuizService.js";
import Quiz_create_info from "@/components/create_quiz/quiz_create_info.vue";
import Quiz_create_question from "@/components/create_quiz/quiz_create_question.vue";
import {ref} from "vue";
import router from "@/router/index.js";
export default {
  components: {Quiz_create_info, Quiz_create_question},
  setup(){
    const store = useQuizCreateStore();
    const quiz = store.templateQuiz;
    const categories = ref([])

    const saveQuiz = async (question_list) => {
      quiz.questions = question_list.question_list
      let categoryIds = store.templateQuiz.categories.map(category => category.categoryId)

      try {
        await store.createQuiz(quiz);

        let categoriesDTO = {
          quizId : store.templateQuiz.quizId,
          categoryIds : categoryIds
        }
        await addCategories(categoriesDTO)
        setTimeout(async ()=> {await router.push('/home');}, 500);
      } catch (error) {
        console.error(error);
      }
    }

    const addCategory = (category) => {
      categories.value.push(category)
    }

    const removeCategory = (category) => {
      categories.value = categories.value.filter(c => c !== category)
    }


    return {
      store,
      quiz,
      saveQuiz,
      addCategory,
      removeCategory
    };
  }
}


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