<template>
  <div>
    <div class="modal-overlay" v-if="showPopupProp">
      <div class="popup">
        <div class="popup-content">
          <h3>Do you want to delete this?</h3>
          <div class="button-group">
            <button @click="deleteQuestion(question)">Yes</button>
            <button @click="closePopup">No</button>
          </div>
        </div>
      </div>
    </div>
    <div class="question-item">
      <div class="question-info">
        <router-link to="/edit/question" class="router">
          <span>{{ question.question }}</span>
        </router-link>
        <div class="actions">
          <font-awesome-icon
              class="icon"
              icon="fa-solid fa-trash"
              @click="showPopupProp = true"
              v-if="isAuth || isEditor"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {ref, getCurrentInstance} from "vue";
import {useQuizStore} from "@/stores/counter.js";

export default {
  props: {
    question: {
      type: Object,
      default: () => ({
        id: Number,
        question: String,
        answer: String,
        type: String,
      })
    }
  },

  setup() {
    const store = useQuizStore();
    const showPopupProp = ref(false);
    const { emit } = getCurrentInstance();
    const isAuth = ref(store.isAuth)
    const isEditor = ref(store.isEditor)

    const deleteQuestion = (question) => {
      emit("deleteQuestion", question.id);
      showPopupProp.value = false;
    };
    const closePopup = () => {
      showPopupProp.value = false;
    };

    return {
      showPopupProp,
      deleteQuestion,
      closePopup,
      isEditor,
      isAuth
    };
  }
}

</script>

<style>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.popup {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.popup-content {
  display: flex;
  flex-direction: column;
}

.button-group {
  display: flex;
  justify-content: space-evenly;
  margin-top: 10px;
}

.button-group button{
  width: 50%;
}

.router {
  text-decoration: none;
  color: black;
}

.icon:hover {
  color: red;
  scale: 1.25;
  cursor: pointer;
}

.question-item {
  margin-top: 2%;
}

.question-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(231, 231, 231, 0.96);
  padding: 10px;
  border-radius: 5px;
  width: 80%;
  height: 50px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.question-info:hover {
  transition: 0.5s;
  transform: translateY(-5px);
}

span {
  font-weight: bold;
}

.actions {
  display: flex;
  align-items: center;
}

.actions font-awesome-icon {
  margin-left: 10px;
  cursor: pointer;
}

.actions font-awesome-icon:hover {
  scale: 1.25;
  color: #ff001e;
}
</style>
