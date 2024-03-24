<template>
  <div class="rectangle">
    <router-link :to="'/quiz?' + quiz.name">
      <div class="card">
        <img :src="quiz.image" alt="quiz image"/>
        <h4 class="quiz-desc">{{ quiz.name }}</h4>
        <h4 class="quiz-desc">{{ quiz.description }}</h4>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  props: {
    quiz: {
      type: Object,
      default: () => ({
        name: String,
        difficulty: String,
        description: String,
        image: String,
        question_list: Array,
      })
    }
  }
}

</script>

<style scoped>
.rectangle {
  padding-top: 5%;
  margin-right: 50px;
}

.rectangle:hover {
  transform: translateY(-5px);
  transition: 0.5s;
}

.card {
  width: 300px;
  height: 200px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card img {
  width: 100%;
  border-radius: 10px;
  margin-bottom: 10px;
}

.quiz-desc {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

</style>
