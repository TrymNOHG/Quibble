<template>
  <div class="comp">
    <div class="buttons">
      <button>{{ $t('buttons.ONE_PLAYER') }}</button>
      <button>{{ $t('buttons.MULTI_PLAYER') }}</button>
      <router-link class="btn" to="/home" v-if="isAuthor" @click="deleteQuiz()">{{ $t('buttons.DELETE_QUIZ') }}</router-link>
    </div>
    <div class="header"></div>
    <div class="questions_list">
      <div class="buttons bottomline" >
        <button v-if="isEditor || isAuthor">{{ $t('buttons.NEW_QUESTION') }}</button>
        <h2 class="question_text">{{ $t('titles.QUESTION_LIST') }}</h2>
        <button v-if="isEditor || isAuthor">{{ $t('buttons.IMPORT_QUESTION') }}</button>
      </div>
      <div class="encap_List">
        <question-list
            class="list"
            v-for="q in question_list"
            :question="q"
            @deleteQuestion="deleteQuestion(q.id)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import QuestionList from "@/components/BasicComponents/questionList.vue";
import {ref} from "vue";
import {useQuizStore} from "@/stores/counter.js";


export default {
  components: {QuestionList},

  setup() {
    const store = useQuizStore();
    const isAuthor = ref(store.isAuth);
    const isEditor = ref(store.isEditor);
    let question_list = ref(store.currentQuiz.question_list);

    const deleteQuestion = (questionId) => {
      store.deleteQuestion(questionId);
    };

    const deleteQuiz = () => {
      store.deleteCurrentQuiz();
    }

    return {
      deleteQuiz,
      isAuthor,
      isEditor,
      question_list,
      deleteQuestion,
    }
  }
}


</script>


<style scoped>
.btn {
  width: 15%;
  height: 35px;
  color: white;
  text-align: center;
  margin-bottom: 3%;
  justify-content: space-between;
  align-content: center;
  background-color: #b22fe8;
  border-radius: 5px;
  font-size: 14px;
  transition: background-color 0.3s;
  text-decoration: none;
  border: 2px solid black;
}

.question_text{
  font-size: 25px;
  font-weight: bold;
}

.encap_List{
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
}

.list {
  margin-left: 18%;
  width: 80%;
}

.buttons {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin: 2%;
}

.header {
  margin-top: 2%;
  border-width: 0 0 1px 0;
  border-style: solid;
  border-color: #cccccc;
}

.bottomline{
  margin-bottom: 0;
}

button{
  width: 15%;
  height: 35px;
  color: white;
  text-align: center;
  margin-bottom: 3%;
  justify-content: space-between;
  align-content: center;
  background-color: #b22fe8;
  border-radius: 5px;
  font-size: 14px;
  transition: background-color 0.3s;
}

button:hover {
  scale: 1.05;
  cursor: pointer;
  background-color: #7e1f9c;
}

@media only screen and (max-width: 428px) {
  button {
    width: 80px;
    height: 50px;
  }
}
</style>
