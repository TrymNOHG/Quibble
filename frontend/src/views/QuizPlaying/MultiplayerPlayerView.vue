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
      <basic_button class="large-button" :button_text="'Join Game'" @click="joinGameAsGuest"></basic_button>
    </div>

    <div v-if="isAuthenticated && gameEnded && !sentFeedback" class="feedback-section">
      <h2>Feedback</h2>
      <form @submit.prevent="submitFeedback">
        <div class="form-group">
          <label for="feedbackRating">Rating:</label>
          <select v-model="feedback.stars" id="feedbackRating">
            <option value="5">⭐⭐⭐⭐⭐</option>
            <option value="4">⭐⭐⭐⭐</option>
            <option value="3">⭐⭐⭐</option>
            <option value="2">⭐⭐</option>
            <option value="1">⭐</option>
          </select>
        </div>
        <div class="form-group">
          <label for="feedbackText">Your Feedback:</label>
          <textarea v-model="feedback.text" id="feedbackText"></textarea>
        </div>
        <button type="submit" class="feedback-submit">Submit Feedback</button>
      </form>
    </div>

    <template v-else>
      <div v-if="joined && joinScreenDisplayed" class="game-feedback joined-success">
        <p>Game has been joined. Get ready!</p>
      </div>
      <div v-if="gameEnded" class="game-feedback game-ended">
        <p>The game has ended. Your final score is {{ score }}.</p>
      </div>
      <div v-else-if="waitingForQuestion" class="game-feedback waiting-for-question">
        <p>Waiting for the next question... stay tuned!</p>
      </div>
      <div v-else-if="waitingForAnswer" class="game-feedback waiting-for-answer">
        <p>Waiting for the answer to be revealed...</p>
      </div>
      <div v-else-if="answerRevealed" class="game-feedback answer-feedback" :class="{'correct-answer': isAnswerCorrect, 'incorrect-answer': !isAnswerCorrect}">
        <p v-if="isAnswerCorrect">You answered correctly! 🎉</p>
        <p v-else>You answered incorrectly. 😞</p>
        <p>Your total score: {{ score }}</p>
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
            @answerSelected="handleAnswer"
        />
        <TruthOrFalseComponent
            v-else-if = "question.questionType === 'TRUE_FALSE'"
            :question="question"
            :isSinglePlayer="false"
            :is-multiplayer-client="true"
            @answerSelected="handleAnswer"
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
import {addFeedback} from "@/services/feedbackService.js";


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
    const joinScreenDisplayed = ref(true);
    const sentFeedback = ref(false);

    const userStore = useUserStore();
    const feedback = ref({
      stars: 5,
      text: ""
    });

    const submitFeedback = async () => {
      if (!isAuthenticated.value) {
        alert("You must be logged in to submit feedback.");
        return;
      }

      const feedbackDTO = {
        quizId: quizid.value, // The ID of the current quiz
        userId: userStore.user.userId, // The ID of the user
        stars: feedback.value.stars, // The star rating
        feedback: feedback.value.text // The feedback text
      };

      try {
        await addFeedback(feedbackDTO);
        alert("Feedback submitted successfully.");
        sentFeedback.value = true;
      } catch (error) {
        console.error("Failed to submit feedback:", error);
        alert("There was a problem submitting your feedback.");
      }
    };


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
        joinScreenDisplayed.value = true
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
        joinScreenDisplayed.value = false;
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
      gameService.onQuizId( (response) => {
        quizid.value = response;
      });
      // Implement event listeners as shown in the previous example
    };

    const handleAnswer = async (answer) => {
      console.log("Answering question", answer)
      if (isAuthenticated.value) {
        const answerDto = {
          answer: answer,
          code: gameId.value,
          jwt: await sessionToken(),
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
      joinScreenDisplayed,
      sentFeedback,
      feedback,
      submitFeedback,
      quizid,
    };
  },
};
</script>

<style scoped>
 .game-client {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   gap: 20px;
 }

 .large-button {
   min-width: 150px; /* Minimum width for the button */
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


 .default-images-container {
   background: #fff;
   padding: 25px;
   border-radius: 15px;
   box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
   text-align: center;
   width: 90%;
   max-width: 960px;
   margin: 0 auto;
   transition: all 0.3s ease;
 }



 .images-grid {
   display: grid;
   grid-template-columns: repeat(auto-fill, minmax(100px, 1fr)); /* Responsive grid */
   gap: 20px;
   padding-top: 20px;
 }

 .image-card {
   cursor: pointer;
   transition: transform 0.2s;
   display: flex;
   justify-content: center;
   align-items: center;
   overflow: hidden; /* Ensure nothing spills outside the border */
   border-radius: 50%;
   border: 3px solid transparent; /* Adjust the border for selected state */
 }

 .image-card.selected {
   border-color: #673AB7;
   transform: scale(1.05);
 }

 .default-avatar {
   width: 100%;
   height: auto; /* Maintain aspect ratio */
   transition: transform 0.2s ease-in-out;
 }

 .image-card:hover .default-avatar {
   transform: scale(1.1); /* Zoom effect on hover */
 }

 .game-client input[type="text"], .game-client basic-button {
   width: 100%;
   padding: 10px;
   margin: 20px 0;
   border-radius: 5px;
   box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
   transition: all 0.3s ease;
 }

 .game-client input[type="text"] {
   border: 2px solid #673AB7; /* Purple border for input fields */
   font-size: 16px; /* Matching font size from the submit form */
 }

 .game-client input[type="text"]:focus {
   border-color: #9C27B0; /* Darker purple when focused */
   outline: none; /* Removing default browser outline */
 }

 .game-client .basic_button {
   background-color: #673AB7; /* Consistent button color */
   color: white;
   padding: 10px 20px;
   cursor: pointer;
   font-size: 16px; /* Consistent font size for readability */
 }

 .game-client .basic_button:hover {
   background-color: #9C27B0; /* Slightly darker on hover for a subtle interactive effect */
 }

 /* Adding consistent styling for labels */
 .game-client label {
   font-size: 16px; /* Consistent font size with input fields for harmony */
   color: #333; /* A color that ensures readability */
   margin-bottom: 5px; /* Providing space between the label and the input field */
 }

 .game-feedback {
   padding: 20px 30px; /* Increased padding for larger appearance */
   border-radius: 15px; /* Slightly larger border-radius for a softer look */
   color: #fff;
   text-align: center;
   font-size: 22px; /* Increased font size for better readability */
   font-weight: bold;
   margin: 20px 0;
   width: 95%;
   max-width: 800px; /* Adjusted max-width for a larger feedback area */
   box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* Enhanced shadow for more depth */
   transition: transform 0.3s ease, box-shadow 0.3s ease; /* Smooth transitions for hover effects */
 }

 .game-feedback:hover {
   transform: translateY(-5px); /* Slight lift effect on hover */
   box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2); /* Increased shadow on hover for more depth */
 }

 /* Specific Styles for Different States */
 .joined-success {
   background-color: #4CAF50; /* Green for success */
   animation: fadeIn 2s;
 }

 .game-ended {
   background-color: #9C27B0; /* Purple for game end */
   animation: fadeIn 2s;
 }

 .waiting-for-question, .waiting-for-answer {
   background-color: #2196F3; /* Blue for waiting */
   animation: pulse 2s infinite;
 }

 .answer-feedback {
   animation: fadeIn 2s;
 }

 .correct-answer {
   background-color: #4CAF50; /* Green for correct answers */
 }

 .incorrect-answer {
   background-color: #F44336; /* Red for incorrect answers */
 }

 /* Keyframes for Animations */
 @keyframes fadeIn {
   from { opacity: 0; }
   to { opacity: 1; }
 }

 @keyframes pulse {
   0% { background-color: #2196F3; }
   50% { background-color: #64B5F6; }
   100% { background-color: #2196F3; }
 }

 .feedback-section {
   background: #fff;
   padding: 20px;
   border-radius: 8px;
   box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
   text-align: center;
   max-width: 500px; /* Adjust width as necessary */
   margin: auto; /* Center the form */
 }

 .feedback-section h2 {
   color: #673AB7;
   margin-bottom: 15px;
 }

 .feedback-section .form-group {
   margin-bottom: 15px; /* Space between form groups */
 }

 .feedback-section label {
   display: block; /* Ensure the label is a block to be above the input/textarea */
   margin-bottom: 5px; /* Space between label and input/textarea */
   color: #333;
 }

 .feedback-section select,
 .feedback-section textarea {
   width: 100%; /* Full width */
   padding: 10px;
   border: 1px solid #ddd;
   border-radius: 4px;
 }

 .feedback-section textarea {
   resize: vertical; /* Allow vertical resize only */
   min-height: 100px; /* Minimum height */
 }

 .feedback-section .feedback-submit {
   width: 100%; /* Full width */
   padding: 10px 0; /* Padding top and bottom */
   font-size: 16px;
   color: white;
   background-color: #673AB7;
   border: none;
   border-radius: 4px;
   cursor: pointer;
   transition: background-color 0.3s ease;
 }

 .feedback-section .feedback-submit:hover {
   background-color: #5e34b1; /* Darker purple on hover */
 }

</style>



