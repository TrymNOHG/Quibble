<template>
  <div class="question-item">
    <div class="question-info">
      <router-link to="/edit/question" class="router">
        <span>{{ question.question }}</span>
      </router-link>
      <div class="actions">
        <font-awesome-icon
            class="icon"
            icon="fa-solid fa-trash"
            @click="showPopup = true"
        />
        <delete_comp :showPopupProp="showPopup" @update:showPopupProp="showPopup = $event" />
      </div>
    </div>
  </div>
</template>

<script>
import Delete_comp from "@/components/BasicComponents/delete_comp.vue";

export default {
  components: {Delete_comp},
  props: {
    question: {
      type: Object,
      default: () => ({
        Id: Number,
        question: String,
        difficulty: String,
        answer: String,
        type: String,
      })
    }
  },

  data() {
    return {
      showPopup: false
    };
  },

  methods: {
    deleteQuestion() {

    }
  }
}

</script>

<style scoped>
.router{
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

.question-info:hover{
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
