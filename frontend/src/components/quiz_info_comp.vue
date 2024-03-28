<template>
  <div class="quiz">
    <div class="quiz-info">
      <div class="img">
        <img class="quiz-img" :src="quiz.Image" alt="Quiz Image"/>
      </div>
      <div class="quiz-details">
        <h2>{{ quiz.Name }}</h2>
        <p><strong>Difficulty:</strong> {{ quiz.Difficulty }}</p>
        <p>{{ quiz.Description }}</p>
      </div>
    </div>

    <div class="authors">
      <div class="header">
        <h2>Authors:</h2>
        <font-awesome-icon
            id="add"
            icon="fa-solid fa-circle-plus"
            @click="showPopUP()"
            v-if="isEditor || isAuthor"
        />
      </div>
      <listing_comp
          v-for="(a, index) in quizAuthors"
          :key="index"
          :author="a"
          @deleteAuthor="deleteAuthor(a)"
      />
      <div class="popup" v-if="showPopup">
        <div class="popup-content">
          <h3>Add New Author</h3>
          <div class="input-group">
            <input type="text" placeholder="Username" v-model="newAuthor.username"/>
          </div>
          <div class="button-group">
            <button @click="addAuthor">Add Author</button>
            <button @click="closePopup">Cancel</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useQuizStore } from "@/stores/counter.js";
import Listing_comp from "@/components/BasicComponents/authorList.vue";
import QuestionList from "@/components/BasicComponents/questionList.vue";

export default {
  components: {QuestionList, Listing_comp },

  props: {
    quiz: {
      QuizId: null,
      Name: "",
      Difficulty: "",
      Description: "",
      Image: "",
      question_list: [
        { id: null,
          question: "",
          answer: "",
          type: "" },
      ],
    },
  },

  setup() {
    const showPopup = ref(false);
    const store = useQuizStore();

    const isAuthor = ref(store.isAuth);
    const isEditor = ref(store.isEditor);
    let quizAuthors = ref(store.allAuthors)


    const newAuthor = {
      username: '',
      role: 'editor'
    };

    const deleteAuthor = (author) => {
      quizAuthors = store.deleteAuth(author);
    };

    const addAuthor = () => {
      console.log('Adding new author:', newAuthor);
      quizAuthors = store.addAuthor(newAuthor);
      newAuthor.username = '';
      newAuthor.role = 'editor';
      showPopup.value = false;
    };

    const closePopup = () => {
      newAuthor.username = '';
      newAuthor.role = 'editor';
      showPopup.value = false;
    };

    const showPopUP = () => {
      showPopup.value = true;
    };

    return {
      quizAuthors,
      deleteAuthor,
      showPopUP,
      showPopup,
      addAuthor,
      closePopup,
      newAuthor,
      isEditor,
      isAuthor
    };
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.popup-content {
  display: flex;
  flex-direction: column;
  background-color: #fefefe;
  margin: 20% auto;
  padding: 20px;
  border-radius: 5px;
  width: 20%;
}

.input-group {
  margin-bottom: 10px;
}

.input-group input {
  width: 100%;
  height: 25px;
}

.input-group select {
  width: 100%;
  height: 25px;
}

.button-group {
  display: flex;
  justify-content: space-evenly;
  margin-top: 10px;
}

.button-group button{
  width: 100px;
}

.header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-content: center;
  align-items: center;
}

#add {
  scale: 1.5;
}

#add:hover {
  scale: 1.8;
  cursor: pointer;
  color: #71da11;
}

.quiz {
  display: grid;
  grid-template-rows: 1fr 1fr;
  padding: 20px;
  background-color: #fff;
  border-width: 0 1px 0 0;
  border-style: solid;
  border-color: #ccc;
}

.quiz-info {
  border-width: 0 0 1px 0;
  border-style: solid;
  border-color: #ccc;
}

.img {
  margin-right: 20px;
}

.quiz-img {
  width: 150px;
  height: auto;
}

.quiz-details {
  flex: 1;
}

.quiz-details h2 {
  margin-top: 0;
  margin-bottom: 10px;
}

.quiz-details p {
  margin: 0 0 10px;
}

.authors {
  margin-top: 10px;
}

@media only screen and (max-width: 428px) {
  .popup-content {
    width: 80%;
  }
}
</style>
