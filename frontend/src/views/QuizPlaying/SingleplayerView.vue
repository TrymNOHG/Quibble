<template>
  <div class="single-player-game">
    <h1>{{ currentQuiz.quizName }}</h1>

    <div v-if="!gameStarted">
      <basic-button @click="startGame" :button_text="startGameText" class="large-button"></basic-button>
    </div>

    <!-- Display score component with countdown here -->
    <ScoreComponentOnePlayer
        v-if="showScoreComponent"
        :player="user"
        :score="score"
        @countdownEnded="handleCountdownEnded"
    >
    </ScoreComponentOnePlayer>

    <PreviewQuestion
        v-else-if="previewPhase"
        :question="currentQuestion"
        @timerDone="handlePreviewEnd"
        @previewEnded="handlePreviewEnd"
    />
    <div v-else-if="gameStarted && !gameEnded && !showScoreComponent">
      <component :is="currentComponent"
                 :question="currentQuestion"
                 :isSinglePlayer="true"
                 @answerSelected="handleAnswer"
                 :showAnswersProp="showAnswers"
                 @timerDone="timeOut"
      >
      </component>
    </div>

    <div v-if="gameEnded" class="game-end-feedback">
      <h2>{{ gameOverTitle }}</h2>
      <p class="final-score">{{ finalScoreText }} {{ score }}</p>

      <!-- Feedback Form -->
      <div v-if="showFeedbackForm" class="feedback-form-container">
        <h3>{{ feedbackTitle }}</h3>
        <form @submit.prevent="submitFeedback">
          <label for="stars">{{ feedbackRatingLabel }}</label>
          <select id="stars" v-model="feedback.stars">
            <option value="5">⭐⭐⭐⭐⭐</option>
            <option value="4">⭐⭐⭐⭐</option>
            <option value="3">⭐⭐⭐</option>
            <option value="2">⭐⭐</option>
            <option value="1">⭐</option>
          </select>

          <label for="feedbackText">{{ feedbackTextLabel }}</label>
          <textarea id="feedbackText" v-model="feedback.feedbackText" placeholder="Your feedback..."></textarea>

          <basic-button :button_text="submitFeedbackText" @click="submitFeedback" class="feedback-button"></basic-button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {ref, computed, onMounted, onUnmounted, onBeforeUnmount} from 'vue';
import {useQuizStore, useUserStore} from "@/stores/counter.js";
import BasicButton from "@/components/BasicComponents/basic_button.vue";
import sessionToken from "@/features/SessionToken.js";
import ShortAnswer from "@/components/QuizPlaing/shortAnswercomponent.vue";
import MultipleChoice from "@/components/QuizPlaing/mutlipleChoiceComponent.vue";
import TruthOrFalseComponent from "@/components/QuizPlaing/TruthOrFalseComponent.vue";
import router from "@/router/index.js";
import ScoreComponentOnePlayer from "@/components/QuizPlaing/scoreComponentOnePlayer.vue";
import PreviewQuestion from "@/components/QuizPlaing/PreviewQuestion.vue"; // Add this line
import { addHistoricalEvent } from "@/services/HistoryService.js";
import {addFeedback} from "@/services/feedbackService.js";
import {useI18n} from "vue-i18n"; // Import the service function

export default {
  components: {
    PreviewQuestion,
    MultipleChoice,
    ShortAnswer,
    TruthOrFalseComponent,
    BasicButton,
    ScoreComponentOnePlayer
  },
  setup() {
    const {t} = useI18n();
    const store = useQuizStore();
    const userStore = useUserStore();
    const user = computed(() => userStore.user);
    console.log(store.currentQuiz)
    if (!store.currentQuiz) {
      router.push('/home');
    }

    const startGameText = computed(() => 'Start Game'); // Assuming static text or you can use t function for translation
    const gameOverTitle = computed(() => t('single_player.gameOver'));
    const finalScoreText = computed(() => t('single_player.finalScore'));
    const feedbackTitle = computed(() => t('single_player.feedbackTitle'));
    const feedbackRatingLabel = computed(() => t('quiz_client.feedback_rating'));
    const feedbackTextLabel = computed(() => t('quiz_client.feedback_text'));
    const submitFeedbackText = computed(() => 'Submit Feedback')

    const currentQuiz = computed(() => store.currentQuiz);
    const gameStarted = ref(false);
    const gameEnded = ref(false);
    const currentQuestionIndex = ref(0);
    //const currentQuestion = computed(() => currentQuiz.value.questions[currentQuestionIndex.value]);
    const showAnswers = ref(false);
    const score = ref(0);
    const showScoreComponent = ref(false);
    const previewPhase = ref(false);

    const scoreSent = ref(false);

    const showFeedbackForm = ref(false);
    const feedback = ref({
      stars: 5, // Default to 5 stars
      feedbackText: '' // User's feedback text
    });

    const sendScoreToHistory = async () => {
      if (gameEnded.value && !scoreSent.value) {
        const historyEvent = {
          quizId: currentQuiz.value.quizId, // assuming the quiz object has an 'id' property
          userId: user.value.userId, // assuming the user object has an 'id' property
          score: score.value
        };
        try {
          await addHistoricalEvent(historyEvent);
          scoreSent.value = true; // Prevents multiple sends
          console.log("Score successfully sent to history");
        } catch (error) {
          console.error("Failed to send score to history", error);
        }
      }
    };


    const currentComponent = computed(() => {
      switch (currentQuestion.value.type) {
        case 'MULTIPLE_CHOICE': return 'MultipleChoice';
        case 'SHORT_ANSWER': return 'ShortAnswer';
        case 'TRUE_FALSE': return 'TruthOrFalseComponent';
        default: return null;
      }
    });

    const shuffleQuestions = (questions) => {
      for (let i = questions.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [questions[i], questions[j]] = [questions[j], questions[i]]; // Swap elements
      }
      return questions;
    };

    const startGame = () => {
      if (currentQuiz.value && currentQuiz.value.questions.length > 0) {
        // Shuffle the questions array
        currentQuiz.value.questions = shuffleQuestions(currentQuiz.value.questions);
      }

      gameStarted.value = true;
      previewPhase.value = true;
      currentQuestionIndex.value = 0; // Reset question index to start from the first question
    };

    const handleAnswer = (isCorrect, timeLeft) => {
      if (isCorrect) {
        // Calculate elapsed time
        const seconds = 90 - timeLeft;

        // Calculate score for this question based on the time taken to answer
        const scoreForQuestion = Math.round(1000 * (1 - seconds / 90));
        // Add the score for this question to the total score
        score.value += scoreForQuestion;
      }

      showAnswers.value = true;
      // Wait 2 seconds before showing the score component
      setTimeout(() => {
        showScoreComponent.value = true;
      }, 2000);
    };

    const transformQuestionFormat = (questions) => {
      return questions.map(question => {
        // Check if the question is of type MULTIPLE_CHOICE before transforming
        if (question.type === 'MULTIPLE_CHOICE') {
          return {
            ...question,
            options: question.choices.map(choice => ({
              alternative: choice.alternative,
              isCorrect: choice.isCorrect
            }))
          };
        } else {
          // For other types, return the question unchanged
          return question;
        }
      });
    };

    const currentQuestion = computed(() => {
      if (currentQuiz.value && currentQuiz.value.questions.length > 0 ) {

        // Transform the format of the current question
        const transformedQuestions = transformQuestionFormat(currentQuiz.value.questions);
        return transformedQuestions[currentQuestionIndex.value];
      }
      return null;
    });

    const nextQuestion = () => {
      if (currentQuestionIndex.value < currentQuiz.value.questions.length - 1) {
        previewPhase.value = true;
        currentQuestionIndex.value++;
        showAnswers.value = false;
      } else {
        gameEnded.value = true;
        sendScoreToHistory();
        showFeedbackForm.value = true  // Call this function when the game ends
      }
    };

    const handleCountdownEnded = () => {
      nextQuestion();
      showScoreComponent.value = false;
    };

    const timeOut = () => {
      console.log("Time out")
      showAnswers.value = true;
      setTimeout(() => {
        showScoreComponent.value = true;
      }, 2000);
    };

    const handlePreviewEnd = () => {
      previewPhase.value = false;
    };

    const submitFeedback = async () => {
      const feedbackDTO = {
        quizId: currentQuiz.value.quizId, // The ID of the current quiz
        userId: user.value.userId, // The ID of the user
        stars: feedback.value.stars, // The star rating
        feedback: feedback.value.feedbackText // The feedback text
      };

      try {
        console.log("Sending feedback...")
        await addFeedback(feedbackDTO);
        console.log("Feedback successfully sent");
        showFeedbackForm.value = false; // Hide the feedback form after submission
      } catch (error) {
        console.error("Failed to send feedback", error);
      }
    };

    return {
      currentQuiz,
      gameStarted,
      gameEnded,
      currentQuestion,
      startGame,
      handleAnswer,
      currentComponent,
      showAnswers,
      score,
      nextQuestion,
      handleCountdownEnded,
      showScoreComponent,
      user,
      previewPhase,
      handlePreviewEnd,
      showFeedbackForm,
      feedback,
      submitFeedback,
      timeOut,
      t,
      startGameText,
      gameOverTitle,
      finalScoreText,
      feedbackTitle,
      feedbackRatingLabel,
      feedbackTextLabel,
      submitFeedbackText,
    };
  }
};
</script>

<style scoped>
.single-player-game {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.large-button {
  min-width: 150px; /* Minimum width for the button */
  padding: 15px 30px; /* Larger padding for better touch */
  font-size: 1rem; /* Larger font size for better readability */
  cursor: pointer; /* To indicate the button is clickable */
  border: none; /* Assuming you don't want a border, but style as needed */
  border-radius: 8px; /* Rounded corners */
  background-color: #673AB7; /* Purple background to match your theme */
  color: white; /* Text color */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Optional: Adds a shadow for depth */
  transition: background-color 0.3s ease; /* Smooth transition for hover effect */
}

.large-button:hover {
  background-color: #5e34b1; /* Slightly darker purple on hover for feedback */
}

.game-end-feedback {
  padding: 20px 30px;
  border-radius: 15px;
  background-color: #673AB7; /* A purple background to match the buttons */
  color: #fff;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  width: 95%;
  max-width: 600px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  margin: 20px auto; /* Center it horizontally */
  transform: translateY(-5px);
  transition: all 0.3s ease;
}

.game-end-feedback h2 {
  margin: 0 0 20px 0; /* Add some spacing around the h2 element */
  color: #FFD700; /* Gold color to highlight the end of the game */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

.final-score {
  font-size: 18px;
  margin-bottom: 20px; /* Space before the restart button */
}

/* Feedback Form Styles */
.feedback-form-container {
  margin-top: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.feedback-form-container h3 {
  margin-bottom: 15px;
  color: #673AB7;
}

.feedback-form-container form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.feedback-form-container label {
  margin-bottom: 5px;
  font-weight: bold;
  color: #333;
}

.feedback-form-container select,
.feedback-form-container textarea {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
}

.feedback-form-container textarea {
  resize: vertical;
  min-height: 100px;
}

/* Feedback Button Styles */
.feedback-button {
  padding: 10px 20px;
  font-size: 16px;
  color: white;
  background-color: #673AB7;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.feedback-button:hover {
  background-color: #5e34b1;
}
</style>
