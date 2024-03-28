<template>
  <div class="comp">
    <div class="buttons">
      <button>one-player</button>
      <button>multi-player</button>
      <button>delete quiz</button>
    </div>
    <div class="header"></div>
    <div class="questions_list">
      <div class="buttons bottomline">
        <button>new question</button>
        <h2 class="question_text">Question List</h2>
        <button>import question</button>
      </div>
      <div class="encap_List">
        <question-list
            class="list"
            v-for="q in questions"
            :question="q"
        />
      </div>
    </div>
  </div>
</template>


<script setup>
import QuestionList from "@/components/BasicComponents/questionList.vue";

const questions = [
  { id: 1, question: "What is your name?", answer: "John", type: "text" },
  { id: 2, question: "How old are you?", answer: "25", type: "number" },
  { id: 3, question: "What is your favorite color?", answer: "Blue", type: "text" },
  { id: 3, question: "What is your favorite color?", answer: "Blue", type: "text" },
  { id: 3, question: "What is your favorite color?", answer: "Blue", type: "text" },
];

</script>


<style>
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
  margin-left: 20%;
  width: 80%;
}

.buttons {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin: 5%;
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
</style>
