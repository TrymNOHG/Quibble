<template>
  <div>
    <div class="modal-overlay" v-if="showPopupProp">
      <div class="popup">
        <div class="popup-content">
          <h3>{{ $t('delete_modal.TITLE') }}</h3>
          <div class="button-group">
            <button @click="deleteAuthor(author)">{{ $t('delete_modal.YES') }}</button>
            <button @click="closePopup">{{ $t('delete_modal.NO') }}</button>
          </div>
        </div>
      </div>
    </div>
    <div class="author-info">
      <router-link :to="'/userprofile'" class="author-link">
        <span>{{ author.username }}</span>
      </router-link>
      <div class="actions">
        <font-awesome-icon class="trash-icon" icon="fa-solid fa-trash" @click="showPopupProp = true" v-if="isEditor || isAuth"/>
      </div>
    </div>
  </div>
</template>

<script>
import {ref, getCurrentInstance} from "vue";
import {useQuizStore} from "@/stores/counter.js";

export default {
  props: {
    author: {
      type: Object,
      required: true,
      default: () => ({
        userId: Number,
        username: String,
      })
    }
  },

  setup() {
    const store = useQuizStore();
    const showPopupProp = ref(false);
    const { emit } = getCurrentInstance();
    const isAuth = ref(store.isAuth)
    const isEditor = ref(store.isEditor)

    const deleteAuthor = (author) => {
      emit("deleteAuthor", author);
      showPopupProp.value = false;
    };

    const closePopup = () => {
      showPopupProp.value = false;
    };

    return {
      showPopupProp,
      deleteAuthor,
      closePopup,
      isAuth,
      isEditor
    };
  }
}
</script>

<style scoped>
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

.author-link {
  text-decoration: none;
  color: black;
}

.trash-icon:hover {
  color: red;
  scale: 1.25;
  cursor: pointer;
}

.author-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #4991fa;
  padding: 10px;
  border-radius: 5px;
  width: 80%;
  height: 50px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 2%;
  color: white;
}

.author-info:hover {
  transition: 0.5s;
  transform: translateY(-5px);
}

span {
  font-weight: bold;
  color: white;
}

.actions {
  display: flex;
  align-items: center;
  align-content: center;
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
