<template>
  <div class="quiz">
    <div class="quiz-info">
      <div class="img">
        <img class="quiz-img" :src="quiz.Image" alt="Quiz Image"/>
      </div>
      <div class="quiz-details">
        <div class="quiz-detail-header">
          <h2>{{quiz.quizName}}</h2>
          <font-awesome-icon
              id="edit"
              icon="fa-solid fa-pen-to-square"
              @click="editQuizInfo()"
              v-if="(isEditor || isAuthor)"
          />
        </div>
        <div>
          <input v-if="isEditing" class="input-area" type="text" id="quizName" v-model="quizUpdateDTO.newName">
        </div>
        <div>
          <p>
            <strong>{{ $t('dropdown_options.DIFFICULTY') }}:</strong>
            {{ $t('dropdown_options.' + quiz.difficulty) }}
          </p>
          <select v-if="isEditing" class="input-area" id="difficulty" v-model="quizUpdateDTO.difficulty">
            <option value="Easy">{{ $t('dropdown_options.EASY') }}</option>
            <option value="Medium">{{ $t('dropdown_options.MEDIUM') }}</option>
            <option value="Hard">{{ $t('dropdown_options.HARD') }}</option>
          </select>
        </div>
        <div>
          <p>{{ $t('quiz_card.QUESTIONS_LABEL') }}: {{ quiz.questions.length }}</p>
        </div>
        <div>
          <p>{{ $t('quiz_card.DESCRIPTION') }}: {{ quiz.quizDescription }}</p>
          <textarea v-if="isEditing" class="input-area" id="description" v-model="quizUpdateDTO.newDescription"></textarea>
        </div>
        <button v-if="isEditing" class="input-area" @click="saveEdit()">Save edit</button>
      </div>
    </div>
    <div class="authors">
      <div class="header">
        <h2>{{ $t('titles.AUTHORS') }}:</h2>
        <font-awesome-icon
            id="add"
            icon="fa-solid fa-circle-plus"
            @click="showPopUP()"
            v-if="isEditor || isAuthor"
        />
      </div>
      <listing_comp
          v-for="(a, index) in quiz.collaborators"
          :key="index"
          :author="a"
          @deleteAuthor="deleteAuthor(a)"
          :isAuthor="isAuthor"
          :isEditor="isEditor"
      />
      <div class="popup" v-if="showPopup">
        <div class="popup-content">
          <div class="popup-header">
            <h3>{{ $t('titles.ADD_NEW_AUTHOR') }}</h3>
            <div @click="closePopup()" class="close-icon">X</div>
          </div>
          <div class="input-group">
            <input type="text" :placeholder="'username'" v-model="searchQuery" @input="username"/>
          </div>
          <div class="user-list">
            <user_list
                v-for="author in collaboratorList.users"
                :key="author.id"
                :user-data="author"
                @adduser="addAuthor(author)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {onMounted, ref, watch} from "vue";
import {useQuizStore, useUserStore} from "@/stores/counter.js";
import Listing_comp from "@/components/BasicComponents/authorList.vue";
import user_list from "@/components/user_list.vue"


export default {
  components: { Listing_comp, user_list },

  props: {
    quiz: {
      type: Object,
      default: () => ({
        quizId: Number,
        quizName: String,
        difficulty: String,
        quizDescription: String,
        adminId: Number,
        feedbacks: Set,
        collaborators: Set,
        categories: Set,
        questions: Set,
        keywords: Set,
        Image: String,
      })
    }
  },

  setup() {
    const showPopup = ref(false);
    const userStore = useUserStore();
    const store = useQuizStore();
    let isAuthor = ref(true);
    let isEditor = ref(true);
    let isEditing = ref(false);
    let quizAuthors = ref(store.currentQuiz.collaborators === null ? [] : store.currentQuiz.collaborators);
    let collaboratorList = ref([]);
    const searchQuery = ref('');

    let quizUpdateDTO = {
      "quizId": store.currentQuiz.quizId,
      "newName":  store.currentQuiz.quizName,
      "newDescription": store.currentQuiz.quizDescription,
      "difficulty": store.currentQuiz.difficulty
    }

    const editQuizInfo = () => {
      isEditing.value = true;
    };

    const saveEdit = async () => {
      try {
        console.log(searchQuery.value)
        return await store.updateCurrentQuiz(quizUpdateDTO);
      } catch (error) {
        console.error('Error editing quiz:', error);
      }
      isEditing.value = false;
    };

    onMounted( () => {
      isAuthor.value = store.isAdmin(store.currentQuiz.adminId)
      checkEditor();
    });

    watch(searchQuery, async () => {
      if (searchQuery.value !== "") {
        collaboratorList.value = await filteredUsers();
      } else {
        collaboratorList.value = [];
      }
    });

    const filteredUsers = async () => {
      try {
        console.log(searchQuery.value)
        return await store.filterAuthor(searchQuery.value);
      } catch (error) {
        console.error('Error editing question:', error);
      }
    };

    const checkEditor = () => {
      isEditor.value = quizAuthors.value.some(author => author.userId === userStore.user.userId);
    };

    const quizAuthorDTO = {
      quizId: null,
      username: '',
    };

    const deleteAuthor = (author) => {
      store.deleteAuth(author);
    };

    const addAuthor = (author) => {
      quizAuthors = store.addAuthor(author);
      quizAuthorDTO.username = '';
      showPopup.value = false;
    };

    const closePopup = () => {
      searchQuery.value = '';
      showPopup.value = false;
    };

    const showPopUP = () => {
      showPopup.value = true;
    };

    return {
      quizAuthors,
      quizUpdateDTO,
      editQuizInfo,
      isEditing,
      deleteAuthor,
      showPopUP,
      showPopup,
      addAuthor,
      closePopup,
      quizAuthorDTO,
      isEditor,
      isAuthor,
      searchQuery,
      filteredUsers,
      collaboratorList,
      saveEdit
    };
  }
}
</script>

<style scoped>

.input-area {
  width: 80%;
  height: 40px;
  font-size: medium;
}

#edit {
  scale: 1.5;
}

.quiz-detail-header {
  display: flex;
  flex-direction: row;
  align-content: center;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: space-between;
}

.user-list {
  max-height: 200px;
  overflow-y: auto;
}

.popup-header {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
}

.close-icon {
  background-color: #8521b0;
  color: white;
  border-radius: 50px;
  width: 25px;
  text-align: center;
}

.close-icon:hover {
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
  margin-right: 0;
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
  margin-top: 10px;
  background-color: rgba(205, 205, 205, 0.96);
  border-radius: 10px;
  height: 200px;
  width: 320px;
  margin-right: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
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
