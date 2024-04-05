<template>
  <div class="game-client">
    <div v-if="!isAuthenticated">
      <p>Please enter your name to join the game:</p>
      <input v-model="playerName" placeholder="Enter name">
      <basic_button :button_text="'Join Game'" @click="joinGameAsGuest"></basic_button>
    </div>

    <template v-else>
      <div v-if="!joined">
        <basic_button :button_text="'Join Game'" @click="joinGame"></basic_button>
      </div>

      <div v-if="waitingForQuestion">
        <p>Waiting for the next question...</p>
      </div>

      <div v-if="question">
        <!-- Dynamically render question based on type -->

      </div>

      <div v-if="showScore">
        <p>Your score: {{ score }}</p>
      </div>
    </template>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import gameService from "@/services/GameService.js";
import { useRoute } from 'vue-router';
import {useUserStore} from "@/stores/counter.js";
import BasicButton from "@/components/BasicComponents/basic_button.vue";
import sessionToken from "@/features/SessionToken.js";


export default {
  components: {
    BasicButton,
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
    const showScore = ref(false);
    const score = ref(0);

    const joinGame = async () => {
      const jwt = await sessionToken(); // Your method to get JWT
      gameService.joinGameWithJWT(gameId.value, jwt, (response) => {
        joined.value = true;
        listenToEvents();
      });
    };

    const joinGameAsGuest = () => {
      if (playerName.value.trim()) {
        gameService.joinGameAsGuest(gameId.value, playerName.value, (response) => {
          joined.value = true;
          listenToEvents();
        });
      } else {
        alert("Please enter a name to join the game.");
      }
    };

    const listenToEvents = () => {
      // Implement event listeners as shown in the previous example
    };

    const handleAnswer = (answer) => {
      // Handle the answer
    };

    const getQuestionComponent = (type) => {
      // Determine the component based on question type
    };

    return {
      isAuthenticated,
      playerName,
      joined,
      waitingForQuestion,
      question,
      showScore,
      score,
      joinGame,
      joinGameAsGuest,
      handleAnswer,
      getQuestionComponent,
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
</style>
