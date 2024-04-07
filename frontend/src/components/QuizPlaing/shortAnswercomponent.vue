<template>
  <div class="flashcard-container">
    <!-- Question Card -->
    <div v-if="!isMultiplayerClient" class="question-card">
      <div  class="card-content" v-html="question.question"></div>
    </div>

    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: progressBarWidth + '%'}"></div>
    </div>

    <!-- Input for Short Answer -->
    <!-- Include multiplayer check to potentially hide input in client view mode -->
    <div v-if="(isSinglePlayer || isMultiplayerClient) && !showAnswersProp " class="answer-input-container">
      <input v-model="userAnswer" type="text" :placeholder="t('new_question.answer_label')" :disabled="showAnswersProp"/>
      <button @click="submitAnswer" :disabled="showAnswersProp">{{submitButtonLabel}}</button>
    </div>





    <!-- Feedback -->
    <div v-if="showFeedback && isSinglePlayer" :class="{'correct-feedback': isAnswerCorrect, 'incorrect-feedback': !isAnswerCorrect}">
      <p>{{ feedbackMessage }}</p>
    </div>


    <div v-if="!isSinglePlayer && showAnswersProp" class="multiplayer-feedback-container">
      <div class="multiplayer-feedback"> {{multiplayerFeedbackMessage}}:<strong>{{ question.answer }}</strong>. <br>
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
    isMultiplayerClient: { // Added prop for multiplayer client
      type: Boolean,
      default: false
    },
  },
  setup(props, { emit, expose }) {
    const userAnswer = ref('');
    const showFeedback = ref(false);
    const isAnswerCorrect = ref(false);
    const feedbackMessage = ref('');
    const timeLeft = ref(90);
    const progressBarWidth = ref(100);
    const timer = ref(null);


    const {t} = useI18n();

    const answerPlaceholder = computed(() => t('new_question.answer_label'));
    const submitButtonLabel = computed(() => t('placeholders.SUBMIT'));
    const multiplayerFeedbackMessage = computed(() => t('quiz_client.answer'));

    const correctSound = new Audio(correctSoundFile);
    const wrongSound = new Audio(wrongSoundFile);
    const timerSound = new Audio(timerSoundFile);

    // ...existing setup logic

    const playSound = (sound) => {
      sound.currentTime = 0; // Reset the sound to start
      sound.play()
    };

    // Start the timer when the component is mounted
    onMounted(() => {
      startTimer();
      if (!props.isMultiplayerClient) playSound(timerSound);
    });

    // Clean up the timer when the component is unmounted
    onUnmounted(() => {
      stopTimer();
    });

    watch(() => props.showAnswersProp, (newValue) => {
      if (newValue)
      stopTimer()
      emit('timerDone');
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
      timerSound.pause();
    };

    const handleTimeOut = () => {
      stopTimer();
      // Handle what happens when the timer runs out
      showFeedback.value = true;
      feedbackMessage.value = `${t('quiz_client.timeUp')} ${props.question.answer}.`;
      //add tranlation on the line above: feedbackMessage.value = t('quiz_client.timeUp', {answer: props.question.answer});
      isAnswerCorrect.value = false;
      playSound(wrongSound);
      emit('timerDone');
    };

    // Submit answer function
    const submitAnswer = () => {
      // Stop the timer when the user submits an answer
      stopTimer();
      showFeedback.value = true;

      // Check if it's a single-player game or the client is not in multiplayer mode
      if (props.isSinglePlayer) {
        const answerCorrect = userAnswer.value.toLowerCase() === props.question.answer.toLowerCase();
        isAnswerCorrect.value = answerCorrect;
        feedbackMessage.value = t('quiz_client.correct_answer');
        if (!answerCorrect) {
          feedbackMessage.value = `${t('quiz_client.incorrect_answer2')} ${props.question.answer}.`;
        }
        // Emit the result along with the time left if it's not a multiplayer client view
        if (!props.isMultiplayerClient) {
          if (answerCorrect) {
            playSound(correctSound);
          } else {
            playSound(wrongSound);
          }
          emit('answerSelected', answerCorrect, timeLeft.value);
        }
      } else {
        // For multiplayer clients, handle submission differently or emit a different event
        // This could involve emitting the answer directly for validation elsewhere
        emit('answerSelected', userAnswer.value);
      }
    };

    return {
      userAnswer,
      showFeedback,
      isAnswerCorrect,
      feedbackMessage,
      submitAnswer,
      progressBarWidth,
      t,
      answerPlaceholder,
      submitButtonLabel,
      multiplayerFeedbackMessage
    };
  }
};
</script>


<style scoped>
/* Styles adjusted for visibility and adding correct answer animation */


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
  display: flex;
  flex-direction: column;
}

.answer-input-container input{
  width: 100%;
  margin: 10px;

}

.answer-input-container button{
  width: 100%;
  margin: 10px;

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
