<template>
  <div class="flashcard-container">
    <!-- Question Card -->
    <div class="question-card">
      <div class="card-content" v-html="question.question"></div>
    </div>

    <!-- Temp button to show answers for demo purposes -->
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>

    <!-- Answer Cards Container for True or False -->
    <div class="answers-container">
      <div class="answer-card true-card"
           :class="{'flip': showAnswers, 'correct': isCorrect(true), 'incorrect': !isCorrect(true)}"
           @click="selectAnswer(true)">
        <div class="card-front">True</div>
        <div class="card-back">
          <span v-if="isCorrect(true)">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
      <div class="answer-card false-card"
           :class="{'flip': showAnswers, 'correct': isCorrect(false), 'incorrect': !isCorrect(false)}"
           @click="selectAnswer(false)">
        <div class="card-front">False</div>
        <div class="card-back">
          <span v-if="isCorrect(false)">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, watch, onMounted, onUnmounted } from 'vue';

export default {
  props: {
    question: {
      type: Object,
      required: true
    },
    isSinglePlayer: {
      type: Boolean,
      default: true
    },
    showAnswersProp: {
      type: Boolean,
      default: false
    },
    duration: {
      type: Number,
      default: 90 // Set default duration of the timer in seconds
    }
  },
  setup(props, { emit }) {
    const showAnswers = ref(props.showAnswersProp);
    const selectedAnswer = ref(null);
    const correctAnswer = ref(props.question.answer === "true");
    const timeLeft = ref(props.duration);
    const progressBarWidth = ref(100);

    const timer = ref(null);

    const isCorrect = (choice) => {
      return (choice === true && correctAnswer.value) || (choice === false && !correctAnswer.value);
    };

    const selectAnswer = (choice) => {
      if (!props.isSinglePlayer || showAnswers.value) return;

      selectedAnswer.value = choice;
      showAnswers.value = true;
      stopTimer();

      if (isCorrect(choice)) {
        emit('answerSelected', true);
      } else {
        emit('answerSelected', false);
      }
    };

    const startTimer = () => {
      stopTimer();
      timer.value = setInterval(() => {
        if (timeLeft.value > 0) {
          timeLeft.value--;
          progressBarWidth.value = (timeLeft.value / props.duration) * 100;
        } else {
          handleTimeOut();
        }
      }, 1000);
    };

    const stopTimer = () => {
      clearInterval(timer.value);
    };

    const handleTimeOut = () => {
      stopTimer();
      showAnswers.value = true;
      emit('timerDone');
    };

    watch(() => props.showAnswersProp, (newValue) => {
      showAnswers.value = newValue;
      if (newValue) {
        stopTimer();
        emit('timerDone');
      }
    });

    onMounted(() => {
      startTimer();
    });

    onUnmounted(() => {
      stopTimer();
    });

    return {
      showAnswers,
      selectedAnswer,
      timeLeft,
      progressBarWidth,
      selectAnswer,
      isCorrect,
      startTimer,
      stopTimer
    };
  }
};
</script>
<style scoped>
.flashcard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%; /* Use 100% of the parent container's width */
}

/* If you want the question card to be the same size as the answers and progress bar */
.question-card {
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  width: 100%; /* Set to 100% to match the parent if the parent's width is controlled elsewhere */
  box-sizing: border-box; /* Include padding in the width */
}

.progress-bar-container {
  width: 500px; /* Match the width of the question card */
  background-color: #e0e0e0;
  border-radius: 5px;
  margin: 10px 0;
  padding: 0; /* Remove padding to ensure the bar spans the full width */
}

.progress-bar {
  height: 10px;
  background-color: #4CAF50;
  border-radius: 5px;
  width: 100%; /* ensure it fills the container */
}

.answers-container {
  display: flex;
  justify-content: space-around;
  width: 500px; /* Match the width of the question card */
  max-width: inherit; /* Inherit the max-width from the parent container if set */
}

.answer-card {
  flex: 1; /* Use flexbox to give equal space to both cards */
  max-width: 50%; /* Ensure neither card is more than half the parent's width */
  height: 120px;
  cursor: pointer;
  border-radius: 15px;
  color: #fff;
  font-weight: bold;
  align-items: center;
  justify-content: center;
  display: flex;
  position: relative;
  box-sizing: border-box; /* Include padding and border in the width */
  margin: 0 10px; /* Optional margin for spacing between cards */
}
.card-front, .card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  backface-visibility: hidden;
  border-radius: 15px;
  font-size: 1.5em;
}

.card-back {
  background-color: #9e9e9e;
  transform: rotateY(180deg);
}

.flip {
  transform: rotateY(180deg);
}

.correct .card-back {
  background-color: #4CAF50;
}

.incorrect .card-back {
  background-color: #f44336;
}

.true-card .card-front {
  background-color: #03a9f4;
}

.false-card .card-front {
  background-color: #e91e63;
}
</style>