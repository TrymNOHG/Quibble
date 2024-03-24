<template>
  <div class="rectangle">
    <router-link :to="'/quiz?' + quiz.name">
      <div class="card">
        <div class="content">
          <img class="card_image" :src="quiz.image" alt="quiz image"/>
          <div class="information">
            <h4 class="quiz-name">{{ quiz.name }}</h4>
            <p class="quiz-details">Questions: {{ quiz.question_list.length }}</p>
          </div>
        </div>
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
  height: 260px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card .content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.information {
  text-align: center;
}

.information .quiz-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.information .quiz-details {
  font-size: 14px;
  bottom: 0;
  left: 0;
  margin: 0;
}

.quiz-details{
  text-align: right;
  text-decoration: none;
}

.card img {
  width: 100%;
  max-height: 50%;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 10px;
}

@media only screen and (max-width: 428px) {
  .rectangle {
    padding-top: 5%;
    margin: 0;
  }

  .card {
    width: 280px;
    height: 210px;
    margin: 0;
  }

  .quiz-name,
  .quiz-details {
    font-size: 12px;
  }

  .card_image{
    padding: 0;
    width: 280px;
    height: 250px;
  }

  .quiz-details {
    display: none;
  }
}
</style>
