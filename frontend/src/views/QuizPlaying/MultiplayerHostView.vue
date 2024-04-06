<template>
  <div class="game-host">
    <h1>{{quizName}}</h1> <!todo quiz name>

    <basic_button v-if="!gameStarted && !gameCode" @click="createGame" :button_text="$t('createGame')"></basic_button>
    <p v-else> Game Code: {{ gameCode }}</p>
    <basic_button v-if="gameCode && !gameStarted" @click="startGame":button_text="$t('startGame')"></basic_button>

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
    <playerPreviewComponent :players="players" v-if="players.length > 0 && !gameStarted" />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
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
    const quizName = ref("Quiz Name"); // todo get quiz name
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
        quizName
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
</style>
