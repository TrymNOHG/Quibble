<template>
  <div class="flashcard-container">
    <div class="question-card" v-if="!isMultiplayerClient">
      <div class="card-content" v-html="question.question"></div>
    </div>
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>
    <div class="answers-container">
      <!-- Modified to include multiplayer mode checks -->
      <div class="answer-card true-card"
           :class="{'flip': showAnswers && !isMultiplayerClient, 'correct': isMultiplayerClient ? false : isCorrectTrue && showAnswers, 'incorrect': isMultiplayerClient ? false : !isCorrectTrue && showAnswers}"
           @click="selectAnswer(true)">
        <div class="card-front">{{ trueTranslation }}</div>
        <div class="card-back" v-if="!isMultiplayerClient">
          <span v-if="isCorrectTrue">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
      <div class="answer-card false-card"
           :class="{'flip': showAnswers && !isMultiplayerClient, 'correct': isMultiplayerClient ? false : isCorrectFalse && showAnswers, 'incorrect': isMultiplayerClient ? false : !isCorrectFalse && showAnswers}"
           @click="selectAnswer(false)">
        <div class="card-front">{{ falseTranslation }}</div>
        <div class="card-back" v-if="!isMultiplayerClient">
          <span v-if="isCorrectFalse">✓</span>
          <span v-else>✕</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {ref, watch, onMounted, onUnmounted, computed} from 'vue';
import correctSoundFile from "@/assets/sound/correct.mp3";
import wrongSoundFile from "@/assets/sound/wrong.mp3";
import timerSoundFile from "@/assets/sound/timer.mp3";
import {useI18n} from "vue-i18n";

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
    },
    duration: {
      type: Number,
      default: 90
    },
    isMultiplayerClient: { // Added prop for multiplayer client
      type: Boolean,
      default: false
    },
  },
  setup(props, { emit }) {
    const showAnswers = ref(props.showAnswersProp);
    const selectedAnswer = ref(null);
    const correctAnswer = ref(props.question.answer === "true");
    const timeLeft = ref(props.duration);
    const progressBarWidth = ref(100);
    const timer = ref(null);

    const correctSound = new Audio(correctSoundFile);
    const wrongSound = new Audio(wrongSoundFile);
    const timerSound = new Audio(timerSoundFile);

    const {t} = useI18n();

    const trueTranslation = computed(() => t('true_false.true'));
    const falseTranslation = computed(() => t('true_false.false'));

    const isCorrectTrue = computed(() => {
      return correctAnswer.value === true;
    });

    const isCorrectFalse = computed(() => {
      return correctAnswer.value === false;
    });

    const playSound = (sound) => {
      sound.currentTime = 0; // Reset to start
      sound.play()
    };

    const isCorrect = (choice) => {
      return (choice === true && correctAnswer.value) || (choice === false && !correctAnswer.value);
    };

    const selectAnswer = (choice) => {
      // Adjusted for multiplayer mode
      if (!props.isSinglePlayer && !props.isMultiplayerClient || showAnswers.value) return;

      selectedAnswer.value = choice;
      showAnswers.value = true;
      stopTimer();

      if (!props.isMultiplayerClient) {
        if (isCorrect(choice)) {
          playSound(correctSound);
          emit('answerSelected', true, timeLeft.value);
        } else {
          playSound(wrongSound);
          emit('answerSelected', false, timeLeft.value);
        }
      } else {
        // For multiplayer clients, handle differently or emit different event
        emit('answerSelected', choice);
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
      timerSound.pause();
    };

    const handleTimeOut = () => {
      stopTimer();
      showAnswers.value = true;
      playSound(wrongSound);
      emit('timerDone');
    };

    watch(() => props.showAnswersProp, (newValue) => {
      showAnswers.value = newValue;
      stopTimer()
      emit('timerDone');
    });

    onMounted(() => {
      startTimer();
      if (!props.isMultiplayerClient) playSound(timerSound);
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
      stopTimer,
      handleTimeOut,
      t,
      isCorrectFalse,
      isCorrectTrue,
      trueTranslation,
      falseTranslation,
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
  transform-style: preserve-3d;
  display: flex;
  transition: transform 0.6s;
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