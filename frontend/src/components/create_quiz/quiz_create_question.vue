<template>
  <div class="modal-overlay" v-if="addNewQuestion">
    <div class="popup">
      <div class="popup-content">
        <h2>New Question</h2>
        <div class="popup_input">
          <label for="question">Question Name:</label>
          <input class="input" type="text" v-model="newQuestion.name" id="question">
        </div>
        <div class="popup_input">
          <label for="question_type">Question Type:</label>
          <select class="input" v-model="newQuestion.type" id="question_type">
            <option class="input" value="truefalse">True or False</option>
            <option class="input" value="multiplechoice">Multiple Choice</option>
          </select>
        </div>
        <div class="truefalse" v-if="newQuestion.type==='truefalse'">
          <div class="popup_input">
            <label for="answer">Answer:</label>
            <select class="input-truefalse" v-model="newQuestion.answer" id="answer">
              <option class="input" value="true">True</option>
              <option class="input" value="false">False</option>
            </select>
          </div>
        </div>
        <div class="multiple" v-else-if="newQuestion.type==='multiplechoice'">
          <div v-for="(answer, index) in newQuestion.answers" :key="index" class="answer-option">
            <label :for="'answer' + index">Answer {{ index + 1 }}</label>
            <input type="text" v-model="newQuestion.answers[index]" :id="'answer' + index" class="input answer">
            <input type="checkbox" v-model="newQuestion.correctAnswers[index]">
          </div>
        </div>
        <div class="button-group">
          <button class="popbtn" @click="createQuestion()">Create</button>
          <button class="popbtn" @click="cancelCreate">Cancle</button>
        </div>
      </div>
    </div>
  </div>
  <div class="comp">
    <div class="buttons">
      <router-link class="btn" to="/myquiz" @click="createQuiz()">Create Quiz</router-link>
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
            @deleteQuestion="deleteQuestion(q.id)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import QuestionList from "@/components/BasicComponents/questionList.vue";
import {ref} from "vue";
import {useQuizCreateStore} from "@/stores/counter.js";
import QuestionCreateList from "@/components/create_quiz/question-create-list.vue";


export default {
  components: {QuestionCreateList, QuestionList},

  setup() {
    const store = useQuizCreateStore();
    const addNewQuestion = ref(false);
    let question_list = ref(store.templateQuiz.question_list);

    let newQuestion = ref({
      name: '',
      type: 'true_false',
      answer: 'true',
      answers: ['Option 1', 'Option 2', 'Option 3', 'Option 4'],
      correctAnswers: [false, false, false, false]
    });

    const cancelCreate = () => {
      newQuestion.value.name = '';
      newQuestion.value.type = 'true_false';
      newQuestion.value.answer = 'true';
      newQuestion.value.answers = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
      newQuestion.value.correctAnswers = [false, false, false, false];
      addNewQuestion.value = false;
    };

    return {
      question_list,
      cancelCreate,
      addNewQuestion,
      newQuestion,
    }
  }
}


</script>


<style scoped>
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
  align-items: baseline;
  flex-wrap: wrap;
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
