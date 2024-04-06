<template>
  <div class="game-client">
    <div v-if="!isAuthenticated && !joined && gameExists">
      <div class="default-images-container">
        <h2>Select Your Avatar</h2>
        <div class="images-grid">
          <div v-for="imageId in defaultImages" :key="imageId" class="image-card" :class="{ 'selected': imageId === defaultImageChoice }" @click="selectImage(imageId)">
            <img :src="getPictureURL(imageId)" alt="Default Avatar" class="default-avatar">
          </div>
        </div>
      </div>
      <p>Please enter your name to join the game:</p>
      <input v-model="playerName" placeholder="Enter name">
      <basic_button :button_text="'Join Game'" @click="joinGameAsGuest"></basic_button>
    </div>

    <template v-else>
      <div v-if="gameEnded">
        <p>game has ended your score is {{ score }}</p>
      </div>
      <div v-else-if="waitingForQuestion">
        <p>Waiting for the next question... look at the screen</p>
      </div>
      <div v-else-if="waitingForAnswer">
        <p>Waiting for the answer to be revealed...</p>
      </div>
      <div v-else-if="answerRevealed">
        <p v-if="isAnswerCorrect">You answered correctly!</p>
        <p v-else>You answered incorrectly!</p>
        <p>Your score: {{ score }}</p>
      </div>

      <div v-else-if="question">
        <MultipleChoice
            v-if = "question.questionType === 'MULTIPLE_CHOICE'"
            :question="question"
            :isSinglePlayer="false"
            :is-multiplayer-client="true"
            @answerSelected="handleAnswer"
        />
        <ShortAnswer
            v-else-if = "question.questionType === 'SHORT_ANSWER'"
            :question="question"
            :isSinglePlayer="false"
            :is-multiplayer-client="true"
        />
        <TruthOrFalseComponent
            v-else-if = "question.questionType === 'TRUE_FALSE'"
            :question="question"
            :isSinglePlayer="false"
            :is-multiplayer-client="true"
        />

      </div>


    </template>
  </div>
</template>

<script>
import {ref, computed, onMounted, onUnmounted, onBeforeUnmount} from 'vue';
import gameService from "@/services/GameService.js";
import { useRoute } from 'vue-router';
import {useUserStore} from "@/stores/counter.js";
import BasicButton from "@/components/BasicComponents/basic_button.vue";
import sessionToken from "@/features/SessionToken.js";
import { getPictureFromID } from "@/services/ImageService.js";
import Basic_button from "@/components/BasicComponents/basic_button.vue"; // Assuming this function exists
import multipleChoiceComponent from "@/components/QuizPlaing/mutlipleChoiceComponent.vue";
import ShortAnswer from "@/components/QuizPlaing/shortAnswercomponent.vue";
import MultipleChoice from "@/components/QuizPlaing/mutlipleChoiceComponent.vue";
import TruthOrFalseComponent from "@/components/QuizPlaing/TruthOrFalseComponent.vue";
import router from "@/router/index.js";


export default {
  components: {
    TruthOrFalseComponent, MultipleChoice, ShortAnswer,
    Basic_button,
    BasicButton,
    multipleChoiceComponent,
  },
  setup() {
    const route = useRoute();
    const gameId = ref(route.params.gameId);
    const store = useUserStore();
    const isAuthenticated = computed(() => store.isAuth);

    const playerName = ref('');
    const joined = ref(false);
    const waitingForQuestion = ref(false);
    const question = ref(null);
    const score = ref(0);
    const defaultImages = ref([]);
    const defaultImageChoice = ref('');

    const waitingForAnswer = ref(false); // New property
    const answerRevealed = ref(false); // New property
    const isAnswerCorrect = ref(false); // New property
    const quizid = ref('');
    const gameEnded = ref(false);
    const gameExists = ref(false);


    const selectImage = (imageId) => {
      defaultImageChoice.value = imageId;
    };

    const getPictureURL = (imageId) => {
      return getPictureFromID(imageId);
    };

    const joinGameAsGuest = () => {
      if (playerName.value.trim() && defaultImages.value.includes(defaultImageChoice.value) && defaultImages.value.length > 0)  {
        const joinGameDTO = {
          username: playerName.value,
          code: gameId.value,
          imageId: defaultImageChoice.value, // Replace with actual image ID
        }
        gameService.joinGame(joinGameDTO, (response) => {
        });
      } else {
        alert("Please enter a name to join the game.");
      }
    };

    const setupListeners = () => {
      gameService.onGameJoined( () => {
        console.log("joined Game")
        waitingForQuestion.value = true;
        joined.value = true;
      });
      gameService.onDefaultImages( (response) => {
        console.log("Default images", response)
        defaultImages.value = response;
        console.log("Default images", defaultImages.value)
      });
      gameService.onbeginAnswering( (response) => {
        console.log("beginAnswering", response)
        answerRevealed.value = false;
        waitingForQuestion.value = false;
        question.value = response;
        waitingForAnswer.value = false;
      });
      gameService.onWaitForQuestion( (response) => {
        console.log("Waiting for question", response)
        waitingForQuestion.value = true;
        question.value = null;
        answerRevealed.value = false;
      });
      gameService.onYourScore( (response) => {
        console.log("Your score", response)
        score.value = response;
      });
      gameService.onAnswerRevealed( (response) => {
        isAnswerCorrect.value = response === true;
        answerRevealed.value = true;
        waitingForAnswer.value = false;
      });
      gameService.onGameEnded( (response) => {
        console.log("Game ended", response)
        gameEnded.value = true;
        // Implement game end logic
      });
      gameService.onQuizId( (response) => {
        console.log("Quiz ID", response)
        quizid.value = response;
      });
      gameService.onGameDoesNotExist( (response) => {
        console.log("Game does not exist", response)
        // Implement game does not exist logic
        router.push("/");
      });
      gameService.onGameExists( (response) => {
        console.log("Game exists", response)
        if (response === false) {
          router.push("/");
        }
        gameExists.value = true
        // Implement game exists logic
      });
      // Implement event listeners as shown in the previous example
    };

    const handleAnswer = (answer) => {
      console.log("Answering question", answer)
      if (isAuthenticated.value) {
        const answerDto = {
          answer: answer,
          code: gameId.value,
          jwt: sessionToken(),
        }
        gameService.answerQuestion(answerDto, (response) => {
          console.log("Answered question", response);
        });
      } else {
        const answerDto = {
          answer: answer,
          code: gameId.value,
        }
        gameService.answerQuestion(answerDto, (response) => {
          console.log("Answered question", response);
        });
      }
      waitingForAnswer.value = true;
    };

    onMounted(async () => {
      await gameService.connect();
      console.log("Setting up listeners")
      await setupListeners();
      console.log("Checking game exists")
      if (isAuthenticated.value) {
        const joingameDTO = {
          jwt: await sessionToken(),
          code: gameId.value,
        }
        console.log("Joining game")
        gameService.joinGame(joingameDTO, (response) => {});
      }
      else {
        const joingameDTO = {
          code: gameId.value,
        }
        await gameService.checkGameExists(joingameDTO, (response) => {});
        console.log("Not authenticated")
      }


      onUnmounted(async () => {
        console.log("Disconnecting")
        await gameService.disconnect();
      });

    });

    return {
      isAuthenticated,
      playerName,
      joined,
      waitingForQuestion,
      question,
      score,
      joinGameAsGuest,
      handleAnswer,
      selectImage,
      getPictureURL,
      defaultImages,
      defaultImageChoice,
      waitingForAnswer,
      answerRevealed,
      isAnswerCorrect,
      gameEnded,
      gameExists,
    };
  },
};
</script>

<style scoped>
 .game-client {
   display: flex;
   flex-direction: column;
   align-items: center;
   gap: 20px;
 }


  .default-images-container {
    background: #fff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
    width: 100%;
    max-width: 960px;
    margin: 20px auto;
  }

 .images-grid {
   display: flex;
   justify-content: center;
   gap: 20px;
 }

 .image-card {
   cursor: pointer;
   transition: transform 0.2s;
   border-radius: 50%; /* Circular images */
   padding: 5px; /* Space for the ring */
 }

 .image-card.selected {
   transform: scale(1.1); /* Scale effect for selected avatar */
   box-shadow: 0 0 0 4px #673AB7; /* Ring effect using box-shadow */
 }

 .default-avatar {
   width: 80px; /* Fixed size */
   height: 80px;
   border-radius: 50%; /* Circular images */
   object-fit: cover; /* Ensure the images cover the area well */
 }

 /* Optional: Add a subtle hover effect for interactivity */
 .image-card:hover:not(.selected) {
   box-shadow: 0 0 0 2px rgba(103, 58, 183, 0.5);
 }


</style>



