<template>
  <div class="flashcard-container">
    <!-- Question Card (hidden for multiplayer clients) -->
    <div class="question-card" v-if="!isMultiplayerClient">
      <div class="card-content" v-html="question.question"></div>
    </div>

    <!-- Progress Bar (remains visible as a timer) -->
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>

    <!-- Answer Cards Container (always visible, but without flip or correct/incorrect class) -->
    <div class="answers-container">
      <div v-for="(choice, index) in question.options" :key="index"
           class="answer-card"
           :class="{ 'flip': showAnswers && !isMultiplayerClient, 'correct': isMultiplayerClient ? false : choice.isCorrect && showAnswers, 'incorrect': isMultiplayerClient ? false : !choice.isCorrect && showAnswers }"
           @click="selectAnswer(choice)">
        <div class="card-front">{{ choice.alternative }}</div>
        <!-- Card back content hidden for multiplayer clients -->
        <div class="card-back" v-if="!isMultiplayerClient">
          <span v-if="choice.isCorrect">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import {ref, watch, onMounted, onUnmounted} from 'vue';
import correctSoundFile from "@/assets/sound/correct.mp3"
import wrongSoundFile from "@/assets/sound/wrong.mp3"
import timerSoundFile from "@/assets/sound/timer.mp3"

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
    showAnswersProp: { // New prop to control the display of answers
      type: Boolean,
      default: false
    },
    isMultiplayerClient: {
      type: Boolean,
      default: false
    },
  },
  setup(props, { emit, expose }) {
    const showAnswers = ref(props.showAnswersProp);
    const selectedAnswer = ref(null);
    const timeLeft = ref(90);
    const progressBarWidth = ref(100);
    const timer = ref(null);

    watch(() => props.showAnswersProp, (newValue) => {
      showAnswers.value = newValue;
      stopTimer()
      emit('timerDone');
    });




    const correctSound = new Audio(correctSoundFile);
    const wrongSound = new Audio(wrongSoundFile);
    const timerSound = new Audio(timerSoundFile);

    const playTimerSound = () => {
      timerSound.currentTime = 0;
      timerSound.play()
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
      wrongSound.play()
      showAnswers.value = true;
      emit('timerDone');
    };


    const selectAnswer = (choice) => {
      if (!props.isSinglePlayer && !props.isMultiplayerClient || showAnswers.value) return;

      stopTimer();
      selectedAnswer.value = choice;
      showAnswers.value = true;
      if (props.isMultiplayerClient) {
        emit('answerSelected', choice.alternative);
        return;
      }
      if (choice.isCorrect) {
        correctSound.play()
        emit('answerSelected', true, timeLeft.value);
      } else {
        wrongSound.play()
        emit('answerSelected', false, timeLeft.value);
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

    watch(() => props.question, resetGame, { deep: true });

    onMounted(() => {
      startTimer();
      if (!props.isMultiplayerClient) playTimerSound();
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
.answer-card:nth-child(5) .card-front { background-color: #9c27b0; } /* Purple */
.answer-card:nth-child(6) .card-front { background-color: #ff5722; } /* Deep Orange */
.answer-card:nth-child(7) .card-front { background-color: #607d8b; } /* Blue Grey */
.answer-card:nth-child(8) .card-front { background-color: #795548; } /* Brown */


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

