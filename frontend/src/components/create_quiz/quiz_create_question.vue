<template>
  <div class="modal-overlay" v-if="addNewQuestion || editQuestion">
    <div class="popup">
      <div class="popup-content">
        <h2>{{ $t('new_question.title') }}</h2>
        <div class="popup_input">
          <label for="question">{{ $t('new_question.question_label') }}:</label>
          <input class="input" type="text" v-model="newQuestion.question" id="question">
        </div>
        <div class="popup_input">
          <label for="question_type">{{ $t('new_question.type_label') }}:</label>
          <select class="input" v-model="newQuestion.type" id="question_type">
            <option class="input" value="truefalse">{{ $t('new_question.true_false_option') }}</option>
            <option class="input" value="multiplechoice">{{ $t('new_question.multiple_choice_option') }}</option>
          </select>
        </div>
        <div class="truefalse" v-if="newQuestion.type==='truefalse'">
          <div class="popup_input">
            <label for="answer">{{ $t('new_question.answer_label') }}:</label>
            <select class="input-truefalse" v-model="newQuestion.answer" id="answer">
              <option class="input" value="true">{{ $t('new_question.true_option') }}</option>
              <option class="input" value="false">{{ $t('new_question.false_option') }}</option>
            </select>
          </div>
        </div>
        <div class="multiple" v-else-if="newQuestion.type==='multiplechoice'">
          <div v-for="(answer, index) in newQuestion.answers" :key="index" class="answer-option">
            <label :for="'answer' + index">{{ $t('new_question.answer_label') }} {{ index + 1 }}</label>
            <input type="text" v-model="newQuestion.answers[index]" :id="'answer' + index" class="input answer">
            <input type="checkbox" v-model="newQuestion.correctAnswers[index]">
          </div>
        </div>
        <div class="button-group">
          <div v-if="addNewQuestion" class="popdiv">
            <button class="createdit" @click="createQuestion()">{{ $t('new_question.create_button') }}</button>
          </div>
          <div v-else-if="editQuestion" class="popdiv">
            <button class="createdit" @click="addEdit()">{{ $t('new_question.edit_button') }}</button>
          </div>
          <button class="popbtn" @click="cancelCreate">{{ $t('new_question.cancel_button') }}</button>
        </div>
      </div>
    </div>
  </div>
  <div class="comp">
    <div class="buttons">
      <router-link class="btn" to="/myquiz" @click="createQuiz()">{{ $t('new_question.create_quiz_button') }}</router-link>
      <font-awesome-icon
          id="add"
          icon="fa-solid fa-circle-plus"
          @click="addNewQuestion=true"
      />
    </div>
    <div class="header"></div>
    <div class="questions_list">
      <div class="buttons bottomline" >
      </div>
      <div class="encap_List">
        <question-create-list
            class="list"
            v-for="q in question_list"
            :question="q"
            @deleteQuestion="deleteQuestion(q)"
            @editQuestion="showEdit(q)"
        />
      </div>
    </div>
  </div>
</template>


<script>
import { ref } from 'vue';
import { useQuizCreateStore } from '@/stores/counter.js';
import QuestionCreateList from "@/components/create_quiz/question-create-list.vue";

export default {
  components: { QuestionCreateList },

  setup() {
    const store = useQuizCreateStore();
    const addNewQuestion = ref(false);
    const editQuestion = ref(false);
    const question_list = ref(store.templateQuiz.question_list);

    const newQuestion = ref({
      id: ref('null'),
      question: ref(''),
      answer: ref('true'),
      answers: ref(['Option 1', 'Option 2', 'Option 3', 'Option 4']),
      correctAnswers: ref([false, false, false, false]),
      type: ref('truefalse')
    });

    const createQuestion = () => {
      question_list.value.push({
        question: newQuestion.value.question,
        answer: newQuestion.value.answer,
        answers: newQuestion.value.answers.slice(),
        correctAnswers: newQuestion.value.correctAnswers.slice(),
        type: newQuestion.value.type
      });
      addNewQuestion.value = false;
      editQuestion.value = false;
    };

    const cancelCreate = () => {
      newQuestion.value.question = '';
      newQuestion.value.type = 'truefalse';
      newQuestion.value.answer = 'true';
      newQuestion.value.answers = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
      newQuestion.value.correctAnswers = [false, false, false, false];
      addNewQuestion.value = false;
      editQuestion.value = false;
    };

    const showEdit = (question) => {
      newQuestion.value.id = question.id;
      newQuestion.value.question = question.question;
      newQuestion.value.type = question.type;
      newQuestion.value.answer = question.answer;
      newQuestion.value.answers = question.answers.slice();
      newQuestion.value.correctAnswers = (question.type === "truefalse") ? null : question.correctAnswers.slice();
      editQuestion.value = true;
    };

    const deleteQuestion = (question) => {
      const index = question_list.value.indexOf(question);
      if (index !== -1) {
        question_list.value.splice(index, 1);
      }
    };

    const addEdit = () => {
      for (let i = 0; i < question_list.value.length; i++) {
        if (question_list.value[i].id === newQuestion.value.id){
          question_list.value[i].question = newQuestion.value.question;
          question_list.value[i].answer = newQuestion.value.answer;
          question_list.value[i].answers = newQuestion.value.answers.slice();
          question_list.value[i].correctAnswers = newQuestion.value.correctAnswers
          !== null ? newQuestion.value.correctAnswers.slice() : null;
          question_list.value[i].type = newQuestion.value.type;
          break;
        }
      }
      newQuestion.value.question = '';
      newQuestion.value.type = 'truefalse';
      newQuestion.value.answer = 'true';
      newQuestion.value.answers = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
      newQuestion.value.correctAnswers = [false, false, false, false];
      addNewQuestion.value = false;
      editQuestion.value = false;
    }

    const createQuiz = () => {
      store.createQuiz(question_list);
    }

    return {
      question_list,
      createQuestion,
      addEdit,
      editQuestion,
      deleteQuestion,
      cancelCreate,
      addNewQuestion,
      newQuestion,
      showEdit,
      createQuiz
    };
  }
};

</script>


<style scoped>

.popup-content{
  width: 350px
}

.popdiv {
  width: 35%;
}

.createdit{
  width: 100%
}

.popbtn {
  width: 35%;
}

.truefalse,
.multiple {
  margin-top: 10%;
}

.button-group {
  width: 100%;
}

.popup-content {
  display: flex;
  flex-direction: column;
}

.truefalse {
  display: flex;
  align-content: start;
}

.popup_input {
  display: flex;
  flex-direction: column;
}

.input {
  height: 25px;
  width: 80%;
}

.input-truefalse {
  height: 25px;
  width: 240%;
}

.answer-option {
  display: flex;
  align-items: center;
  margin: 0 0 5% 0;
}

.answer-option input[type="checkbox"] {
  margin-left: 10px;
}

.btn {
  width: 15%;
  height: 35px;
  color: white;
  text-align: center;
  align-content: center;
  background-color: #b22fe8;
  border-radius: 5px;
  font-size: 14px;
  transition: background-color 0.3s;
  text-decoration: none;
  border: 2px solid black;
}

.btn:hover {
  scale: 1.05;
  cursor: pointer;
  background-color: #7e1f9c;
}

.encap_List{
  margin-bottom: 25px;
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
  justify-content: space-evenly;
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
  justify-content: center;
}

#add {
  scale: 2;
  margin: 2%;
}

#add:hover {
  scale: 2.2;
  margin: 2%;
  color: rgba(113,218,17,0.96);
  cursor: pointer;
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
