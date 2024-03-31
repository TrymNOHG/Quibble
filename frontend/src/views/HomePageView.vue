<template>
  <div class="homepage-format">
    <div class="searchbar">
      <SearchInput
          v-model="searchInput"
          @update:modelValue="handleSearchInput"
          @difficultySelected = "handleDifficulty"
      />
    </div>
    <div class="search_query">
      <One_quiz_rectangle
          v-for="(quiz, index) in displayedQuizzes"
          :key="index"
          :quiz="quiz"
      />
    </div>
    <div id="inf_scroll"/>
  </div>
</template>

<script setup>
import SearchInput from "@/components/searchbar.vue";
import {onBeforeMount, onMounted, ref} from 'vue';
import One_quiz_rectangle from "@/components/BasicComponents/one_quiz_rectangle.vue";

const searchInput =  ref('');
const recommendedQuizzes = ref([]);
let displayedQuizzes = ref([]);
let difficulty_selected = ref('');


onBeforeMount(() => {
  //useQuizStore().setAllQuizzes();
  //TODO: get quizzes from backend
  recommendedQuizzes.value = test_quizzes.value;
  displayedQuizzes.value = test_quizzes.value;
})

async function getNextQuiz() {
  window.onscroll = () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      /*
      TODO: get quizzes from backend
      const newQuizzes = useQuizStore().getMoreQuizzes(difficulty_selected);
      quizzes.value = [...newQuizzes]
       */
      const newQuizzes = [
        {
          QuizId: 5,
          Name: "Quiz 5",
          Difficulty: "Medium",
          Description: "Test your knowledge with Quiz 5",
          Image: "https://via.placeholder.com/150",
          question_list: [
            { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
            { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
            { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
          ]
        },
        {
          QuizId: 6,
          Name: "Quiz 6",
          Difficulty: "Hard",
          Description: "Test your knowledge with Quiz 6",
          Image: "https://via.placeholder.com/150",
          question_list: [
            { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
            { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
            { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
          ]
        },
        {
          QuizId: 7,
          Name: "Quiz 7",
          Difficulty: "Easy",
          Description: "Test your knowledge with Quiz 7",
          Image: "https://via.placeholder.com/150",
          question_list: [
            { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
            { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
            { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
          ]
        },
        {
          QuizId: 8,
          Name: "Quiz 8",
          Difficulty: "Medium",
          Description: "Test your knowledge with Quiz 8",
          Image: "https://via.placeholder.com/150",
          question_list: [
            { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
            { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
            { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
          ]
        },
        {
          QuizId: 9,
          Name: "Quiz 9",
          Difficulty: "Hard",
          Description: "Test your knowledge with Quiz 9",
          Image: "https://via.placeholder.com/150",
          question_list: [
            { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
            { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
            { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
          ]
        },
      ];
      displayedQuizzes.value = [...displayedQuizzes.value, ...newQuizzes];
    }
  };
}

async function handleDifficulty(difficulty) {
  difficulty_selected = difficulty
  if (difficulty === "") {
    //TODO: get quizzes from backend
    //const filtered = useQuizStore().getRecommendedDifficulty(difficulty_selected);
    displayedQuizzes.value = recommendedQuizzes.value;
  }
  else {
    //TODO: get quizzes from backend
    //const filtered = useQuizStore().getRecommendedDifficulty(difficulty_selected);
    const filtered = recommendedQuizzes.value.filter(quiz => quiz.Difficulty === difficulty);
    displayedQuizzes.value = filtered;
  }
}

async function handleSearchInput(searchword) {
  if (searchword.length >= 3) {
    //TODO: get quizzes from backend
    //displayedQuizzes.value = await useQuizStore().searchQuizzes(searchword);
    displayedQuizzes.value = [  {
      QuizId: 5,
      Name: "Quiz 5",
      Difficulty: "Medium",
      Description: "Test your knowledge with Quiz 5",
      Image: "https://via.placeholder.com/150",
      question_list: [
        { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
        { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
        { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
      ]
    },
      {
        QuizId: 6,
        Name: "Quiz 6",
        Difficulty: "Hard",
        Description: "Test your knowledge with Quiz 6",
        Image: "https://via.placeholder.com/150",
        question_list: [
          { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
          { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
          { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
        ]
      },
      {
        QuizId: 7,
        Name: "Quiz 7",
        Difficulty: "Easy",
        Description: "Test your knowledge with Quiz 7",
        Image: "https://via.placeholder.com/150",
        question_list: [
          { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
          { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
          { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
        ]
      },
      {
        QuizId: 8,
        Name: "Quiz 8",
        Difficulty: "Medium",
        Description: "Test your knowledge with Quiz 8",
        Image: "https://via.placeholder.com/150",
        question_list: [
          { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
          { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
          { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
        ]
      },
      {
        QuizId: 9,
        Name: "Quiz 9",
        Difficulty: "Hard",
        Description: "Test your knowledge with Quiz 9",
        Image: "https://via.placeholder.com/150",
        question_list: [
          { id: 1, question: "Question 1", answer: "Answer 1", type: "text" },
          { id: 2, question: "Question 2", answer: "Answer 2", type: "text" },
          { id: 3, question: "Question 3", answer: "Answer 3", type: "text" }
        ]
      },];
  } else {
    let newQuizzelist = recommendedQuizzes.value;
    displayedQuizzes.value = newQuizzelist;
  }
}

onMounted(() => {
  getNextQuiz();
});

let test_quizzes = ref([
  {
    QuizId: null,
    Name: "TemplateQuiz",
    Difficulty: "Easy",
    Description: "Template quiz, change the quiz as wanted",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
  {
    QuizId: null,
    Name: "AnotherQuiz",
    Difficulty: "Medium",
    Description: "Another quiz description",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
  {
    QuizId: null,
    Name: "YetAnotherQuiz",
    Difficulty: "Hard",
    Description: "Yet another quiz description",
    Image: "https://via.placeholder.com/150",
    question_list: [
      {
        id: 1,
        question: "What is your question?",
        answer: "John",
        answers: ["pencil", "book", "John", "quiz"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
      {
        id: 2,
        question: "Are you 21 years old?",
        answer: "true",
        answers: ["true", "false"],
        type: "truefalse"
      },
      {
        id: 3,
        question: "What is in the center of the milky way",
        answer: "Black Hole",
        answers: ["Sun", "Earth", "Venus", "Black Hole"],
        correctAnswers: [false, true, false, false],
        type: "multiplechoice"
      },
    ],
  },
]);

</script>

<style scoped>
.homepage-format {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 1%;
}

.searchbar {
  width: 100%;
  align-self: center;
}

.search_query {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  z-index: 0;
}

@media only screen and (max-width: 428px) {
  .search_query {
    display: grid;
    grid-template-columns: 1fr;
    margin: 2%;
    z-index: 0;
  }

  .searchbar {
    width: 100%;
    align-self: center;
  }
}

</style>
