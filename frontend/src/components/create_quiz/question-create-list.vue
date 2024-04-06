<template>
  <div>
    <div class="modal-overlay" v-if="showPopupProp">
      <div class="popup">
        <div class="popup-content">
          <h3>{{ $t('delete_modal.TITLE') }}</h3>
          <div class="button-group">
            <button @click="deleteQuestion">{{ $t('delete_modal.YES') }}</button>
            <button @click="closePopup">{{ $t('delete_modal.NO') }}</button>
          </div>
        </div>
      </div>
    </div>
    <div class="question-item">
      <div class="question-info">
        <input type="checkbox" :checked="selected" @change="toggleQuestion">
        <span>{{ question.question }}</span>
        <div class="actions">
          <font-awesome-icon
              class="icon_edit"
              id="add"
              icon="fa-solid fa-pen-to-square"
              @click="editQuestion"
          />
          <font-awesome-icon
              class="icon_trash"
              icon="fa-solid fa-trash"
              @click="showPopupProp = true"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, getCurrentInstance } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

export default {
  components: { FontAwesomeIcon },
  props: {
    question: {
      type: Object,
      default: () => ({
        id: Number,
        question: String,
        answer: String,
        type: String,
      })
    },
    selected: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const showPopupProp = ref(false);

    const deleteQuestion = () => {
      emit("deleteQuestion", props.question.id);
      showPopupProp.value = false;
    };

    const editQuestion = () => {
      emit("editQuestion", props.question);
    }

    const closePopup = () => {
      showPopupProp.value = false;
    };

    const toggleQuestion = (event) => {
      const isSelected = event.target.checked;
      emit('selectionChange', { question: props.question, isSelected });
    };


    return {
      showPopupProp,
      editQuestion,
      deleteQuestion,
      closePopup,
      toggleQuestion
    };
  },

  methods: {
    deselect(newVal)
    {
      console.log(newVal)
      this.selected = false;
    }
  }
}

</script>

<style>
#add {
  margin-right: 20%;
}

#add:hover {
  scale: 1.2;
  color: rgba(113,218,17,0.96);
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

.icon_trash:hover {
  color: red;
  scale: 1.25;
  cursor: pointer;
}

.icon_trash {
  margin-right: 20%;
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

.bottom {

}
</style>
