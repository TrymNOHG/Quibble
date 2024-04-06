<template>
  <div class="game-client">
    <div v-if="!isAuthenticated && !joined">
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
import {ref, computed, onMounted} from 'vue';
import gameService from "@/services/GameService.js";
import { useRoute } from 'vue-router';
import {useUserStore} from "@/stores/counter.js";
import BasicButton from "@/components/BasicComponents/basic_button.vue";
import sessionToken from "@/features/SessionToken.js";
import { getPictureFromID } from "@/services/ImageService.js";
import Basic_button from "@/components/BasicComponents/basic_button.vue"; // Assuming this function exists


export default {
  components: {
    Basic_button,
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
    const defaultImages = ref([]);
    const defaultImageChoice = ref('');


    const selectImage = (imageId) => {
      defaultImageChoice.value = imageId;
    };

    const getPictureURL = (imageId) => {
      // Assuming getPictureFromID translates ID to URL
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
      // Implement event listeners as shown in the previous example
    };

    const handleAnswer = (answer) => {
      // Handle the answer
    };

    const getQuestionComponent = (type) => {
      // Determine the component based on question type
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

    });

    return {
      isAuthenticated,
      playerName,
      joined,
      waitingForQuestion,
      question,
      showScore,
      score,
      joinGameAsGuest,
      handleAnswer,
      getQuestionComponent,
      selectImage,
      getPictureURL,
      defaultImages,
      defaultImageChoice,
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



