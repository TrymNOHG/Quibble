<template>
  <div class="flashcard-container">
    <!-- Question Card -->
    <div class="question-card">
      <div class="card-content" v-html="question.question"></div>
    </div>

    <!-- Input for Short Answer (hidden in single-player mode) -->
    <div v-if="isSinglePlayer" class="answer-input-container">
      <input v-model="userAnswer" type="text" placeholder="Type your answer here" />
      <button @click="submitAnswer">Submit</button>
    </div>

    <!-- Correct Answer Display (only in single-player mode) -->
    <div v-if="isSinglePlayer" class="correct-answer-container">
      <div class="correct-answer">{{ question.answer }}</div>
    </div>

    <!-- Feedback -->
    <div v-if="showFeedback &&  isSinglePlayer" :class="{'correct-feedback': isAnswerCorrect, 'incorrect-feedback': !isAnswerCorrect}">
      <p>{{ feedbackMessage }}</p>
    </div>

    <!-- Multiplayer Feedback -->
    <div v-if="!isSinglePlayer && showAnswersProp" class="multiplayer-feedback-container">
      <div class="multiplayer-feedback">
        The correct answer is: <strong>{{ question.answer }}</strong>. <br>
      </div>
    </div>

    <!-- Progress Bar -->
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>
  </div>
</template>

<script>
import {ref, watch, onMounted, onUnmounted} from 'vue';

export default {
  props: {
    question: {
      type: Object,
      required: true
    },
    isSinglePlayer: {
      type: Boolean,
      default: false
    },
    showAnswersProp: {
      type: Boolean,
      default: false
    }
  },
  setup(props, { emit, expose }) {
    const userAnswer = ref('');
    const showFeedback = ref(false);
    const isAnswerCorrect = ref(false);
    const feedbackMessage = ref('');
    const timeLeft = ref(90);
    const progressBarWidth = ref(100);
    const timer = ref(null);

    // Start the timer when the component is mounted
    onMounted(() => {
      startTimer();
    });

    // Clean up the timer when the component is unmounted
    onUnmounted(() => {
      stopTimer();
    });

    const startTimer = () => {
      stopTimer(); // Ensure no timers are running
      timer.value = setInterval(() => {
        if (timeLeft.value > 0) {
          timeLeft.value--;
          progressBarWidth.value = (timeLeft.value / 90) * 100;
        } else {
          handleTimeOut();
        }
      }, 1000); // Update every second
    };

    const stopTimer = () => {
      clearInterval(timer.value);
    };

    const handleTimeOut = () => {
      stopTimer();
      // Handle what happens when the timer runs out
      showFeedback.value = true;
      feedbackMessage.value = `Time's up! The correct answer was: ${props.question.answer}`;
      isAnswerCorrect.value = false;
      emit('timerDone');
    };

    // Submit answer function
    const submitAnswer = () => {
      stopTimer(); // Stop the timer when the user submits an answer
      showFeedback.value = true;
      if (userAnswer.value.toLowerCase() === props.question.answer.toLowerCase()) {
        isAnswerCorrect.value = true;
        feedbackMessage.value = "Correct! 🎉";
      } else {
        isAnswerCorrect.value = false;
        feedbackMessage.value = `Incorrect! The correct answer is: ${props.question.answer}`;
      }
    };

    return {
      userAnswer,
      showFeedback,
      isAnswerCorrect,
      feedbackMessage,
      submitAnswer,
      progressBarWidth // Expose progressBarWidth to the template
    };
  }
};
</script>


<style scoped>
/* Styles adjusted for visibility and adding correct answer animation */
.correct-answer-container {
  margin-bottom: 20px;
  padding: 20px;
  background: #4CAF50; /* Green background to indicate correctness */
  color: white; /* White text for better contrast */
  border-radius: 15px;
  width: 80%;
  max-width: 600px;
  opacity: 0;
  animation: fadeIn 2s forwards;
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}


.flashcard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}

.question-card {
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  width: 80%;
  max-width: 600px;
}

.answer-input-container {
  margin-bottom: 20px;
}

input[type="text"] {
  padding: 10px;
  margin-right: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

button {
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  background-color: #4CAF50;
  color: white;
  border: none;
}

.correct-feedback, .incorrect-feedback {
  color: #fff;
  padding: 10px;
  border-radius: 5px;
  margin-top: 10px;
}

.correct-feedback {
  background-color: #4CAF50;
}

.incorrect-feedback {
  background-color: #f44336;
}

.progress-bar-container {
  width: 100%;
  background-color: #e0e0e0;
  border-radius: 5px;
  margin: 10px 0;
}

.progress-bar {
  height: 10px;
  background-color: #4CAF50;
  border-radius: 5px;
}

.multiplayer-feedback-container {
  margin-top: 20px;
  padding: 20px;
  background: linear-gradient(145deg, #CC00FF, #2196F3); /* Gradient to blend with score and fireworks theme */
  color: white;
  border-radius: 15px;
  width: 80%;
  max-width: 600px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  opacity: 0;
  animation: fadeIn 2s forwards;
  position: relative;
  overflow: hidden; /* Ensure nothing spills outside the border-radius */
}

.multiplayer-feedback::after {
  content: "";
  position: absolute;
  top: -50px; right: -50px; bottom: -50px; left: -50px;
  background: repeating-conic-gradient(from 45deg, #ff8, #f08 90deg, #408 180deg, #08f 270deg);
  z-index: -1;
  opacity: 0.2;
  animation: spin 20s linear infinite;
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
