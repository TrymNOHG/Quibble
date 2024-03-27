<template>
  <div class="question-item">
    <div class="question-info">
      <span>{{ question.question }}</span>
      <div class="actions">
        <font-awesome-icon icon="fa-solid fa-trash" @click="deleteQuestion" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
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

  methods: {
    deleteQuestion() {

    }
  }
}

</script>

<style scoped>
.question-item {
  margin-top: 2%;
}

.question-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(19, 155, 250, 0.88);
  padding: 10px;
  border-radius: 5px;
  width: 80%;
  height: 50px;
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
