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
            <option class="input" value="TRUE_FALSE">{{ $t('new_question.true_false_option') }}</option>
            <option class="input" value="MULTIPLE_CHOICE">{{ $t('new_question.multiple_choice_option') }}</option>
          </select>
        </div>
        <div class="true_false" v-if="editQuestion.type==='TRUE_FALSE'">
          <div class="popup_input">
            <label for="answer">{{ $t('new_question.answer_label') }}:</label>
            <select class="input-true_false" v-model="editQuestion.answer" id="answer">
              <option class="input" value="true">{{ $t('new_question.true_option') }}</option>
              <option class="input" value="false">{{ $t('new_question.false_option') }}</option>
            </select>
          </div>
        </div>
        <div class="multiple" v-else-if="editQuestion.type==='MULTIPLE_CHOICE'">
          <div v-for="(choice, index) in editQuestion.choices" :key="index" class="answer-option">
            <label :for="'choice' + index">{{ $t('new_question.answer_label') }} {{ index + 1 }}</label>
            <input type="text" v-model="choice.alternative" :id="'choice' + index" class="input answer">
            <input type="checkbox" v-model="choice.isCorrect">
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
      <button @click="routeSinglePlayer">{{ $t('buttons.ONE_PLAYER') }}</button>
      <button @click="routeMultiPlayer">{{ $t('buttons.MULTI_PLAYER') }}</button>
      <button
          class="btn"
          to="/home"
          v-if="isAuthor"
          @click="deleteQuiz()">
        {{ $t('buttons.DELETE_QUIZ') }}
      </button>
      <button
          class="btn"
          v-if="!isAuthor & !isEditor"
          @click="addToMyquiz()">
        Add to MyQuiz
      </button>
      <div id="download_div">
        <font-awesome-icon
            id="download"
            icon="fa-solid fa-download"
            @click="downloadQuiz()"  />
      </div>
    </div>
    <div class="header"></div>
    <div class="questions_list">
      <div class="buttons bottomline" >
        <button v-if="isEditor || isAuthor" @click="addNew">{{ $t('buttons.NEW_QUESTION') }}</button>
        <h2 class="question_text">{{ $t('titles.QUESTION_LIST') }}</h2>
        <label for="csvFileInput" class="custom-file-upload" v-if="isEditor || isAuthor">
          {{ $t('buttons.IMPORT_QUESTION') }}
          <input id="csvFileInput" type="file" @change="importQuestions" accept=".csv"/>
        </label>
      </div>
      <div class="encap_List">
        <question-list
            class="list"
            v-for="q in question_list"
            :q="q"
            @deleteQuestion="deleteQuestion(q)"
            @editQuestion="showEdit(q)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import QuestionList from "@/components/BasicComponents/questionList.vue";
import { ref } from "vue";
import { useQuizStore } from "@/stores/counter.js";
import QuestionCreateList from "@/components/create_quiz/question-create-list.vue";
import {downloadQuizCSV, uploadQuestionsFromCSV} from "@/features/QuizCSV"
import router from "@/router/index.js";
import Quiz_info_comp from "@/components/quiz_info_comp.vue";


export default {
  components: {Quiz_info_comp, QuestionCreateList, QuestionList },

  props: {
    isAuthor: null,
    isEditor: null,
  },

  setup() {
    const store = useQuizStore();
    const addNewQuestion = ref(false);
    const edit = ref(false);
    let question_list = ref(store.currentQuiz.questions);

    const editQuestion = ref({
      quizId: null,
      questionId: null,
      question:'',
      answer:'',
      type:'TRUE_FALSE',
      choices:[
        { alternative: 'Option 1', isCorrect: false },
        { alternative: 'Option 2', isCorrect: false },
        { alternative: 'Option 3', isCorrect: false },
        { alternative: 'Option 4', isCorrect: false }
      ]
    });

    const deleteQuestion = async (question) => {
      const index = question_list.value.indexOf(question);
      if (index !== -1) {
        question_list.value.splice(index, 1);
      }

      try {
        await store.deleteQuestion(question.questionId);
      } catch (error) {
        console.error('Error editing question:', error);
      }
    };

    const deleteQuiz = async () => {
      try {
        await store.deleteCurrentQuiz();
        setTimeout(() => {
        }, 500);
        await router.push('/home');
      } catch (error) {
        console.error(error);
      }
    }

    const addToMyquiz = () => {
      store.addQuiz();
    }

    const createQuestion = () => {
      if (editQuestion.value.type === 'TRUE_FALSE') {
        editQuestion.value.choices = null;
      } else if (editQuestion.value.type === 'MULTIPLE_CHOICE') {
        const correctChoice = editQuestion.value.choices.find(choice => choice.isCorrect);
        if (correctChoice) {
          editQuestion.value.answer = correctChoice.alternative;
        } else {
          editQuestion.value.answer = '';
        }
      }
      console.log("qwerqwer", editQuestion.value)
      store.addQuestion(editQuestion.value);
      question_list.value.push(editQuestion.value);
      console.log(question_list.value)
      addNewQuestion.value = false;
      edit.value = false;
    };

    const showEdit = (question) => {
      console.log(question)
      editQuestion.value = { ...question };
      edit.value = true;
    };

    const addNew = () => {
      addNewQuestion.value = true;
    };

    const addEdit = async () => {
      const index = question_list.value.indexOf(editQuestion.value);
      if (index !== -1) {
        question_list.value.splice(index, 1);
      }

      try {
        await store.editQuestion(editQuestion.value);
        edit.value = false;
      } catch (error) {
        console.error('Error editing question:', error);
      }
    };

    const cancelCreate = () => {
      editQuestion.value = {
        quizId: null,
        question: "",
        answer: "",
        type: "TRUE_FALSE",
        choices: [
          { alternative: "", isCorrect: false },
          { alternative: "", isCorrect: false },
          { alternative: "", isCorrect: false },
          { alternative: "", isCorrect: false }
        ]
      };
      edit.value = false;
      addNewQuestion.value = false;
    };

    const downloadQuiz = () => {
      console.log("Qwer")
      downloadQuizCSV(store.currentQuiz, store.currentQuiz.quizName)
    }

    const importQuestions = async (event) => {
      let questions = await uploadQuestionsFromCSV(event)
      console.log(questions)
      for (let quest of questions) {
        editQuestion.value = {
          quizId: null,
          questionId: null,
          question: quest.question,
          answer: quest.answer,
          type: quest.type,
          choices: quest.choices
        }
        addNewQuestion.value = true;
        await createQuestion()
      }
      //TODO: add questions
    }

    const routeSinglePlayer = () => {
      router.push('/quiz/singleplayer'); // replace with your actual path
    };

    const routeMultiPlayer = () => {
      router.push('/quiz/multiplayer'); // replace with your actual path
    };

    return {
      addNew,
      cancelCreate,
      showEdit,
      createQuestion,
      addEdit,
      deleteQuiz,
      addToMyquiz,
      question_list,
      deleteQuestion,
      edit,
      editQuestion,
      addNewQuestion,
      downloadQuiz,
      importQuestions,
      routeSinglePlayer,
      routeMultiPlayer
    }
  }
}
</script>



<style scoped>
#download_div{
  width: 35px;
  display: flex;
  height: 35px;
  background-color: rgba(178, 0, 255, 0.1);
  border-radius: 20px;
  flex-direction: row;
  align-content: center;
  align-items: center;
  justify-content: center;
  border: solid black 2px;
}

#download {
  scale: 1.5
}

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

.popup_input {
  display: flex;
  flex-direction: column;
}

.input {
  height: 25px;
  width: 80%;
}

.answer-option {
  display: flex;
  align-items: center;
  margin: 0 0 5% 0;
}

.answer-option input[type="checkbox"] {
  margin-left: 10px;
}

.btn, .custom-file-upload {
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

.btn:hover, .custom-file-upload:hover {
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

button {
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
