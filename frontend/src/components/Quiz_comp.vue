<template>
  <div id="app" class="center-screen">
    <ScoreComponentOnePlayer v-if="showScores" :player="players[0]" />

    <div v-else-if="questionIndex < flashcards.length" class="flashcards-container">
      <FlashCard :flashcard="flashcards[questionIndex]" @answerSelected="handleAnswerSelection" />
      <div class="buttons-container">
        <button class="previous-button" @click="previousQuestion" :disabled="questionIndex === 0">Previous</button>
        <button class="next-button" @click="nextQuestion">Next</button>
      </div>
    </div>

    <div v-else>
      <h2>You've gone through all the flashcards!</h2>
    </div>
  </div>
</template>

<script>
import FlashCard from '@/components/flashcard.vue';
import ScoreComponent from '@/components/scoreComponent.vue';
import ScoreComponentOnePlayer from "@/components/scoreComponentOnePlayer.vue";

export default {
  components: {
    ScoreComponentOnePlayer,
    FlashCard,
    ScoreComponent
  },
  data() {
    return {
      flashcards: [
        // Flashcard data structure with questions, choices, and answers
        {
          "question": "What is the capital of France?",
          "choices": ["Paris", "London", "Berlin", "Madrid"],
          "answer": 0
        },
{
          "question": "What is the largest planet in our solar system?",
          "choices": ["Earth", "Jupiter", "Saturn", "Mars"],
          "answer": 1
        },
{
          "question": "What is the powerhouse of the cell?",
          "choices": ["Nucleus", "Mitochondria", "Ribosome", "Endoplasmic Reticulum"],
          "answer": 1
        },
        {
          "question": "What is the capital of Japan?",
          "choices": ["Beijing", "Seoul", "Tokyo", "Bangkok"],
          "answer": 2
        }
        // ... other flashcard objects
      ],
      questionIndex: 0,
      showScores: false,
      players: [
        { name: 'Brage', score: 0, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQH5idZeFjwtXzC_sCYKeUXhiRlMOi4tp3Ryj8sNP1K4g&s' },
        { name: 'Trym', score: 0, image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVUJj--xz8Y5xKDa1JUDqjQ1U9GtuLdNbN_hitDMTcWw&s' },
        // Add more players if necessary
      ],
    };
  },
  methods: {
    shuffleFlashcards(array) {
      let currentIndex = array.length, temporaryValue, randomIndex;
      // Shuffle logic to randomize the flashcards
      while (0 !== currentIndex) {
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }
      return array;
    },
    nextQuestion() {

      this.showScores = true; // Show scores after an answer is selected
      setTimeout(() => {
        this.showScores = false; // Hide scores after a delay
      }, 10000); // Show the score for 3 seconds
      // Logic to go to the next question
      if (this.questionIndex < this.flashcards.length - 1) {
        this.questionIndex++;
      }
    },
    previousQuestion() {
      // Logic to go back to the previous question
      if (this.questionIndex > 0) {
        this.questionIndex--;
      }
    },
    handleAnswerSelection(isCorrect) {
      // Update player scores here
      if (isCorrect) {
        this.players[0].score += 1; // Increment score for Player 1 if the answer is correct
      }
      // You can implement a mechanism to track which player answered if you have multiple players

    },
  },
  created() {
    this.flashcards = this.shuffleFlashcards(this.flashcards);
  },
}
</script>



<style scoped>
/* Add your CSS styles here */

.center-screen {
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 100vw;
  height: 100vh;
}

.flashcards-containerS {
  width: 100%;
  height: 100vh;
  margin: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.buttons-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  background-color: #ff5f6d;
  color: #fff;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #ff3a4e;
}

h2 {
  color: #ff5f6d;
  font-size: 24px;
  text-align: center;
}

</style>