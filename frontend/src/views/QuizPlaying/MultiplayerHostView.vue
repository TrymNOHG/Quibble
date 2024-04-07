<template>
  <div class="game-host">
    <h1>{{ quizName }}</h1> <!-- Adjusted todo: quiz name -->

    <basic_button class="large-button" v-if="!gameStarted && !gameCode" @click="createGame" :button_text="createGameText"></basic_button>
    <p v-else> {{ gameCodeText }}: {{ gameCode }}</p>
    <basic_button class="large-button" v-if="gameCode && !gameStarted" @click="startGame" :button_text="startGameText"></basic_button>

    <!-- Show current question or game code -->
    <scoreComponent
        v-if="showScoreboard"
        :players="scores"
        :gameEnded="gameEnded"
        @countdownEnded="handleScoreDone"
    />

    <div v-else-if="gameStarted && currentQuestion">
      <PreviewQuestion
          v-if="previewPhase"
          :question="currentQuestion"
          @timerDone="handlePreviewEnd"
          @previewEnded="handlePreviewEnd"
      />
      <MultipleChoice
          v-else-if = "currentQuestion.questionType === 'MULTIPLE_CHOICE'"
          :question="currentQuestion"
          :isSinglePlayer="false"
          :showAnswersProp="showAnswers"
          @timerDone="handleTimerDone"
      />
      <ShortAnswer
          v-else-if = "currentQuestion.questionType === 'SHORT_ANSWER'"
          :question="currentQuestion"
          :isSinglePlayer="false"
          :showAnswersProp="showAnswers"
          @timerDone="handleTimerDone"
      />
      <TruthOrFalseComponent
          v-else-if = "currentQuestion.questionType === 'TRUE_FALSE'"
          :question="currentQuestion"
          :isSinglePlayer="false"
          :showAnswersProp="showAnswers"
          @timerDone="handleTimerDone"
      />
    </div>
    <!-- Show player list -->
    <playerPreviewComponent :players="players" v-if="playerlength > 0 && !gameStarted" />
  </div>
</template>


<script>
import {ref, onMounted, onUnmounted, onBeforeUnmount, computed} from 'vue';
import gameService from "@/services/GameService.js";
import sessionToken from "@/features/SessionToken.js";
import PreviewQuestion from "@/components/QuizPlaing/PreviewQuestion.vue";
import MultipleChoice from '@/components/QuizPlaing/mutlipleChoiceComponent.vue';
import ShortAnswer from '@/components/QuizPlaing/shortAnswercomponent.vue';
import scoreComponent from "@/components/QuizPlaing/scoreComponent.vue";
import basic_button from "@/components/BasicComponents/basic_button.vue";
import playerPreviewComponent from "@/components/QuizPlaing/playerPreviewComponent.vue";
import {useQuizStore} from "@/stores/counter.js";
import router from "@/router/index.js";
import TruthOrFalseComponent from "@/components/QuizPlaing/TruthOrFalseComponent.vue";
import {useI18n} from "vue-i18n";



export default {
  components: {
    PreviewQuestion,
    MultipleChoice,
    scoreComponent,
    ShortAnswer,
    basic_button,
    playerPreviewComponent,
    TruthOrFalseComponent
  },
  setup() {
    const quizName = ref("Quiz Name");
    const gameCode = ref(null);
    const gameStarted = ref(false);
    const gameEnded = ref(false);
    const currentQuestion = ref(null);
    const players = ref([]);
    const previewPhase = ref(false); // Track the preview phase
    const showAnswers = ref(false);
    const showScoreboard = ref(false); // New property for scoreboard display
    const scores = ref([]);
    const quizId = ref(null)// Holds the scores to be displayed
    const store = useQuizStore();
    const {t} = useI18n();

    const createGameText = computed(() => t('createGame'));
    const gameCodeText = computed(() => t('gameCode'));
    const startGameText = computed(() => t('startGame'));
    const playerlength = computed(() => players.length);



    if (!store.currentQuiz) {
      quizId.value = store.currentQuiz.quizId;
      console.log("No quiz selected")
      router.push("/home")
    }

    quizId.value = store.currentQuiz.quizId;
    quizName.value = store.currentQuiz.quizName;



    const createGame = async () => {
      console.log("Creating game")
      // Assuming jwt and quizId are obtained from your auth system and current quiz context
      const jwt = await sessionToken();// Replace with actual JWT token
      gameService.createGame(jwt, quizId.value, (response) => {
      });
    };

    const startGame = async () => {
      const jwt = await sessionToken()
      gameService.startGame(gameCode.value, jwt, (response) => {
        gameStarted.value = true;
      });
    };

    const revealAnswer = async () => {
      const jwt = await sessionToken()
      gameService.revealAnswer(gameCode.value, jwt, (response) => {
      });
    }

    const beginAnswering = async () => {
      const jwt = await sessionToken()
      gameService.beginAnswering(gameCode.value, jwt, (response) => {
        });
    }
    const getScoreBoard = async () => {
      const jwt = await sessionToken();// Replace with actual JWT token
      gameService.getScoreBoard(gameCode.value, jwt, (response) => {
      });
    }

    const nextQuestion = async () => {
      const jwt = await sessionToken()
      gameService.nextQuestion(gameCode.value, jwt, (response) => {
      });
    }


      // Setup WebSocket listeners for game updates
      const setupListeners = () => {
        gameService.onGameCreated((code) => {
          gameCode.value = code;
          console.log(gameCode.value);
        });
        gameService.onPlayerJoined((player) => {
          players.value.push(player);
          console.log("Player joined", player);
        });
        gameService.onPlayerLeft((player) => {
          players.value = players.value.filter((p) => p.id !== player.id);
          console.log("Player left", player);
        });
        gameService.onGetQuestion((question) => {
          previewPhase.value = true;
          currentQuestion.value = question;
          showAnswers.value = false;
          console.log("Current question", question);
        });
        gameService.onAnswerRevealed((answer) => {
          console.log("Answer revealed", answer);
          MultipleChoice.showAnswers = 1; // todo real value
          showAnswers.value = true;
        });
        gameService.onEveryOneAnswered((message) => {
          console.log("Everyone answered")
          console.log(message)
          revealAnswer();
        });
        gameService.onGetScoreBoard((scoreBoard) => {
          scores.value = scoreBoard.player;
        });
        gameService.onGameEnded(() => {
          console.log("Game ended");
          gameEnded.value = true;
          currentQuestion.value = null;
          showScoreboard.value = true;
        });
      };

      onMounted(() => {
        gameService.connect(); // Adjust with your actual WebSocket server URL
        setupListeners();
        console.log("Game service connected", gameService);
      });

      onUnmounted(async () => {
        await gameService.disconnect();
      });



      const handlePreviewEnd = () => {
        previewPhase.value = false;
        beginAnswering();
        console.log("Preview ended")
      };

      const handleTimerDone = () => {
        console.log("Timer done")
        revealAnswer()
        getScoreBoard()
        // wait 6 seconds
        setTimeout(() => {

          showScoreboard.value = true;
          currentQuestion.value = null;

        }, 5000);

        // You can implement score updating or transitioning to the next question here
      };

      const handleScoreDone = () => {
        if (!gameEnded.value) {
          nextQuestion()
          showScoreboard.value = false;
        }
        // Optionally, transition to the next question or end the quiz
      };

      return {
        gameCode,
        gameStarted,
        currentQuestion,
        createGame,
        startGame,
        players,
        previewPhase,
        handlePreviewEnd,
        handleTimerDone,
        showAnswers,
        showScoreboard,
        scores,
        handleScoreDone,
        gameEnded,
        quizName,
        createGameText,
        gameCodeText,
        startGameText,
        playerlength
      };
    }
};
</script>

<style scoped>
.game-host {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}
.large-button {
  min-width: 150px;
  max-width: 250px;/* Minimum width for the button */
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
</style>
