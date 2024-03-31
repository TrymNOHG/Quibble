<template>
  <div class="modal-overlay" v-if="addNewQuestion || edit">
    <div class="popup">
      <div class="popup-content">
        <h2>{{ $t('new_question.title') }}</h2>
        <div class="popup_input">
          <label for="question">{{ $t('new_question.question_label') }}:</label>
          <input class="input" type="text" v-model="editQuestion.question" id="question">
        </div>
        <div class="popup_input">
          <label for="question_type">{{ $t('new_question.type_label') }}:</label>
          <select class="input" v-model="editQuestion.type" id="question_type">
            <option class="input" value="truefalse">{{ $t('new_question.true_false_option') }}</option>
            <option class="input" value="multiplechoice">{{ $t('new_question.multiple_choice_option') }}</option>
          </select>
        </div>
        <div class="truefalse" v-if="editQuestion.type==='truefalse'">
          <div class="popup_input">
            <label for="answer">{{ $t('new_question.answer_label') }}:</label>
            <select class="input-truefalse" v-model="editQuestion.answer" id="answer">
              <option class="input" value="true">{{ $t('new_question.true_option') }}</option>
              <option class="input" value="false">{{ $t('new_question.false_option') }}</option>
            </select>
          </div>
        </div>
        <div class="multiple" v-else-if="editQuestion.type==='multiplechoice'">
          <div v-for="(answer, index) in editQuestion.answers" :key="index" class="answer-option">
            <label :for="'answer' + index">{{ $t('new_question.answer_label') }} {{ index + 1 }}</label>
            <input type="text" v-model="editQuestion.answers[index]" :id="'answer' + index" class="input answer">
            <input type="checkbox" v-model="editQuestion.correctAnswers[index]">
          </div>
        </div>
        <div class="button-group">
          <div v-if="addNewQuestion" class="popdiv">
            <button class="createdit" @click="createQuestion()">{{ $t('new_question.create_button') }}</button>
          </div>
          <div v-else-if="edit" class="popdiv">
            <button class="createdit" @click="addEdit()">{{ $t('new_question.edit_button') }}</button>
          </div>
          <button class="popbtn" @click="cancelCreate">{{ $t('new_question.cancel_button') }}</button>
        </div>
      </div>
    </div>
  </div>
  <div class="comp">
    <div class="buttons">
      <button>{{ $t('buttons.ONE_PLAYER') }}</button>
      <button>{{ $t('buttons.MULTI_PLAYER') }}</button>
      <router-link
          class="btn"
          to="/home"
          v-if="isAuthor"
          @click="deleteQuiz()">
        {{ $t('buttons.DELETE_QUIZ') }}
      </router-link>
      <button
          class="btn"
          v-if="!isAuthor & !isEditor"
          @click="addToMyquiz()">
        Add to MyQuiz
      </button>
    </div>
    <div class="header"></div>
    <div class="questions_list">
      <div class="buttons bottomline" >
        <button v-if="isEditor || isAuthor" @click="addNew">{{ $t('buttons.NEW_QUESTION') }}</button>
        <h2 class="question_text">{{ $t('titles.QUESTION_LIST') }}</h2>
        <button v-if="isEditor || isAuthor">{{ $t('buttons.IMPORT_QUESTION') }}</button>
      </div>
      <div class="encap_List">
        <question-list
            class="list"
            v-for="q in question_list"
            :question="q"
            @deleteQuestion="deleteQuestion(q.id)"
            @editQuestion="showEdit(q)"
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
    const addNewQuestion = ref(false);
    const edit = ref(false);
    const isAuthor = ref(store.isAuth);
    const isEditor = ref(store.isEditor);
    let question_list = ref(store.currentQuiz.question_list);

    const editQuestion = ref({
      id: ref('null'),
      question: ref(''),
      answer: ref('true'),
      answers: ref(['Option 1', 'Option 2', 'Option 3', 'Option 4']),
      correctAnswers: ref([false, false, false, false]),
      type: ref('truefalse')
    });

    const deleteQuestion = (questionId) => {
      store.deleteQuestion(questionId);
    };

    const deleteQuiz = () => {
      store.deleteCurrentQuiz();
    }

    const addToMyquiz = () => {
      store.addQuiz();
    }

    const createQuestion = () => {
      question_list.value.push({
        question: editQuestion.value.question,
        answer: editQuestion.value.answer,
        answers: editQuestion.value.answers.slice(),
        correctAnswers: editQuestion.value.correctAnswers.slice(),
        type: editQuestion.value.type
      });
      store.addQuestion(editQuestion);
      addNewQuestion.value = false;
      edit.value = false;
    };

    const showEdit = (question) => {
      editQuestion.value.id = question.id;
      editQuestion.value.question = question.question;
      editQuestion.value.type = question.type;
      editQuestion.value.answer = question.answer;
      editQuestion.value.answers = question.answers.slice();
      editQuestion.value.correctAnswers = (question.type === "truefalse") ? null : question.correctAnswers.slice();
      edit.value = true;
    };

    const addNew = () => {
      addNewQuestion.value = true;
    };

    const addEdit = () => {
      console.log(edit)
      for (let i = 0; i < question_list.value.length; i++) {
        if (question_list.value[i].id === editQuestion.value.id){
          question_list.value[i].question = editQuestion.value.question;
          question_list.value[i].answer = editQuestion.value.answer;
          question_list.value[i].answers = editQuestion.value.answers.slice();
          question_list.value[i].correctAnswers = editQuestion.value.correctAnswers
          !== null ? editQuestion.value.correctAnswers.slice() : null;
          question_list.value[i].type = editQuestion.value.type;
          break;
        }
      }
      store.editQuestion(editQuestion);
      edit.value = false
    }

    const cancelCreate = () => {
      editQuestion.value.question = '';
      editQuestion.value.type = 'truefalse';
      editQuestion.value.answer = 'true';
      editQuestion.value.answers = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
      editQuestion.value.correctAnswers = [false, false, false, false];
      edit.value = false;
      addNewQuestion.value = false;
    };

    return {
      addNew,
      cancelCreate,
      showEdit,
      createQuestion,
      addEdit,
      deleteQuiz,
      addToMyquiz,
      isAuthor,
      isEditor,
      question_list,
      deleteQuestion,
      edit,
      editQuestion,
      addNewQuestion
    }
  }
}


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
  justify-content: space-between;
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
  justify-content: space-between;
  display: flex;
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
