<template>
  <div class="rectangle">
    <router-link :to="'/quiz/current'" @click="setCurrentQuiz()">
      <div class="card">
        <div class="content">
          <img :src="getPictureURL()" class="card_image" alt="quiz image" />
          <div class="information">
            <h4 class="quiz-name">{{ quiz.quizName }}</h4>
            <p class="quiz-details">{{$t("quiz_card.QUESTIONS_LABEL")}}: {{ quiz.questions.length }}</p>
          </div>
        </div>
      </div>
    </router-link>
  </div>
</template>

<script>
import {useQuizStore} from "@/stores/counter.js";
import {getCurrentInstance} from "vue";
import {getPictureFromID} from "@/services/ImageService.js";
export default {
  props: {
    quiz: {
      type: Object,
      default: () => ({
        quizId: Number,
        quizName: String,
        quizDifficulty: String,
        quizDescription: String,
        admin_id: Number,
        feedbacks: Set,
        collaborators: Set,
        categories: Set,
        questions: Set,
        keywords: Set,
      })
    }
  },

  setup(props) {
    const quizStore = useQuizStore();
    const { emit } = getCurrentInstance();

    const setCurrentQuiz = () => {
      quizStore.currentQuiz = props.quiz
      emit("setCurrentQuiz", props.quiz)
    }

    const getPictureURL = () => {
      const id =`Q${props.quiz.quizId}`
      console.log(getPictureFromID(id))
      return getPictureFromID(id);
    }

    return {
      setCurrentQuiz,
      getPictureURL,
      quizStore,
    }
  },
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
