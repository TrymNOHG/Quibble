<template>
  <div class="flashcard-container">
    <!-- Question Card -->
    <div class="question-card">
      <div class="card-content" v-html="flashcard.question"></div>
    </div>

    <!-- Temp button to show answers for demo purposes -->
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>

    <!-- Answer Cards Container -->
    <div class="answers-container">
      <div v-for="(choice, index) in flashcard.choices" :key="index"
           class="answer-card"
           :class="{'flip': showAnswers, 'correct': isCorrect(index) && showAnswers, 'incorrect': !isCorrect(index) && showAnswers}"
           @click="selectAnswer(index)">
        <div class="card-front">{{ choice }}</div>
        <div class="card-back">
          <span v-if="isCorrect(index)">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import {ref, watch, onMounted, onUnmounted} from 'vue';

export default {
  props: {
    flashcard: {
      type: Object,
      required: true
    },
    isSinglePlayer: {
      type: Boolean,
      default: true
    }
  },
  setup(props, { emit }) {
    const showAnswers = ref(false);
    const selectedAnswer = ref(null);
    const timeLeft = ref(90);
    const progressBarWidth = ref(100);
    const timer = ref(null);

    const correctSound = new Audio("src/assets/sound/correct.mp3");
    const wrongSound = new Audio("src/assets/sound/wrong.mp3");
    const timerSound = new Audio("src/assets/sound/timer.mp3");

    const playTimerSound = () => {
      timerSound.currentTime = 0;
      timerSound.play().catch((e) => console.error('Error playing sound:', e));
    };

    const stopTimer = () => {
      clearInterval(timer.value);
      timerSound.pause();
    };

    const startTimer = () => {
      stopTimer(); // Clear existing timer
      timer.value = setInterval(() => {
        if (timeLeft.value > 0) {
          timeLeft.value -= 1;
          progressBarWidth.value = (timeLeft.value / 90) * 100;
        } else {
          handleTimeOut();
        }
      }, 1000);
    };

    const handleTimeOut = () => {
      stopTimer();
      wrongSound.currentTime = 0; // Ensure the sound starts from the beginning
      wrongSound.play().catch((e) => console.error('Error playing sound:', e));
      showAnswers.value = true;
    };

    const isCorrect = (index) => {
      return index === props.flashcard.answer;
    };

    const selectAnswer = (index) => {
      if (!props.isSinglePlayer || showAnswers.value) return;

      stopTimer();
      selectedAnswer.value = index;
      showAnswers.value = true;

      if (isCorrect(index)) {
        correctSound.play().catch((e) => console.error('Error playing sound:', e));
        emit('answerSelected', true);
      } else {
        wrongSound.play().catch((e) => console.error('Error playing sound:', e));
        emit('answerSelected', false);
      }
    };

    const resetGame = () => {
      showAnswers.value = false;
      selectedAnswer.value = null;
      timeLeft.value = 90;
      progressBarWidth.value = 100;
      startTimer();
      playTimerSound();
    };

    watch(() => props.flashcard, resetGame, { deep: true });

    onMounted(() => {
      startTimer();
      playTimerSound();

      correctSound.loop = false;
      wrongSound.loop = false;
      timerSound.loop = false;
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
    };
  }
};
</script>


<style scoped>
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

.answers-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.answer-card {
  width: 140px;
  height: 140px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  cursor: pointer;
  transform-style: preserve-3d;
  transition: transform 0.6s;
  border-radius: 15px;
  color: #fff; /* White text for better contrast */
}

/* Unique colors for each card, similar to Kahoot! */
.answer-card:nth-child(1) .card-front { background-color: #e91e63; } /* Pink */
.answer-card:nth-child(2) .card-front { background-color: #03a9f4; } /* Light Blue */
.answer-card:nth-child(3) .card-front { background-color: #4caf50; } /* Green */
.answer-card:nth-child(4) .card-front { background-color: #ffa900; } /* Yellow, but w */

.card-front, .card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  backface-visibility: hidden;
  border-radius: 15px;
}

.card-back {
  background-color: #9e9e9e; /* Default back color, can be adjusted as needed */
  transform: rotateY(180deg);
}

.flip {
  transform: rotateY(180deg);
}

.correct .card-back {
  background-color: #4CAF50; /* You might adjust this if you want a specific color for correctness */
}

.incorrect .card-back {
  background-color: #f44336; /* Red for incorrect, though this can also be adjusted */
}
</style>

