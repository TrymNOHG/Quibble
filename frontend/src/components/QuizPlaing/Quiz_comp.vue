<template>
  <div id="app" class="center-screen">
    <!-- Conditionally display score component with countdown -->
    <ScoreComponentOnePlayer
        v-if="showScores"
        :player="players[0]"
        @countdownEnded="handleCountdownEnded"
    />

    <div v-else-if="questionIndex < questions.length" class="flashcards-container">
      <!-- Show preview or multiple-choice based on phase -->
      <PreviewQuestion
          v-if="previewPhase"
          :question="questions[questionIndex]"
          @previewEnded="handlePreviewEnd"
      />
      <MultipleChoice
          v-else
          :question="questions[questionIndex]"
          @answerSelected="handleAnswerSelection"
      />

      <!-- Navigation buttons -->
      <div class="buttons-container">
        <button class="next-button" @click="nextQuestion" :disabled="previewPhase">Next</button>
      </div>
    </div>

    <div v-else>
      <h2>You've gone through all the flashcards!</h2>
    </div>
  </div>
</template>


<script>
import MultipleChoice from '@/components/QuizPlaing/mutlipleChoiceComponent.vue';
import ScoreComponent from '@/components/QuizPlaing/scoreComponent.vue';
import ScoreComponentOnePlayer from "@/components/QuizPlaing/scoreComponentOnePlayer.vue";
import PreviewQuestion from "@/components/QuizPlaing/PreviewQuestion.vue";

export default {
  components: {
    ScoreComponentOnePlayer,
    MultipleChoice,
    ScoreComponent,
    PreviewQuestion

  },
  data() {
    return {
      questions: [
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
      previewPhase: true, // Add this to track the preview phase
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
      this.showScores = true;


      if (this.questionIndex < this.questions.length - 1) {
        this.questionIndex++;
        this.previewPhase = true; // Reset to preview phase for the next question
      } else {
        this.showScores = false; // Optionally handle the end of the quiz
      }
    },
    handleAnswerSelection(isCorrect) {
      // Update player scores and manage transitions
      if (isCorrect) {
        this.players[0].score += 1;
      }
    },
    handlePreviewEnd() {
      this.previewPhase = false;
    },
    handleCountdownEnded() {
      this.showScores = false; // Hide scores and show next question or end quiz
    },
  },
  created() {
    this.shuffleFlashcards(this.questions);
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