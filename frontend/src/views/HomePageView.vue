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
  recommendedQuizzes.value = test_quizzes.value;
  displayedQuizzes.value = test_quizzes.value;
})

async function getNextQuiz() {
  window.onscroll = () => {
    let bottomOfWindow = window.scrollY + window.innerHeight >= document.documentElement.scrollHeight;
    if (bottomOfWindow) {
      /*
      const newQuizzes = useQuizStore().getMoreQuizzes(difficulty_selected);
      quizzes.value = [...newQuizzes]
       */
      const newQuizzes = [
        {
          quizId: 99,
          name: "New Quiz 1",
          difficulty: "Hard",
          description: "Test your knowledge with New Quiz 1",
          image: "https://via.placeholder.com/150",
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
        {
          quizId: 98,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
        {
          quizId: 97,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
        {
          quizId: 96,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        }, {
          quizId: 95,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        }, {
          quizId: 94,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        }, {
          quizId: 93,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        },
        {
          quizId: 92,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
          ]
        }, {
          quizId: 91,
          name: "Quiz 2",
          difficulty: "Medium",
          description: "Test your knowledge with Quiz 2",
          image: "https://via.placeholder.com/150", // Placeholder image URL
          question_list: [
            "Question 1",
            "Question 2",
            "Question 3"
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
    //const filtered = useQuizStore().getRecommendedDifficulty(difficulty_selected);
    displayedQuizzes.value = recommendedQuizzes.value;
  }
  else {
    //const filtered = useQuizStore().getRecommendedDifficulty(difficulty_selected);
    const filtered = recommendedQuizzes.value.filter(quiz => quiz.difficulty === difficulty);
    displayedQuizzes.value = filtered;
  }
}

async function handleSearchInput(searchword) {
  if (searchword.length >= 3) {
    //displayedQuizzes.value = await useQuizStore().searchQuizzes(searchword);
    displayedQuizzes.value = [{
      quizId: 85,
      name: "Quiz 1",
      difficulty: "Easy",
      description: "Test your knowledge with Quiz 1",
      image: "https://via.placeholder.com/150",
      question_list: [
        "Question 1",
        "Question 2",
        "Question 3"
      ]
    },
      {
        quizId: 82,
        name: "Quiz 2",
        difficulty: "Medium",
        description: "Test your knowledge with Quiz 2",
        image: "https://via.placeholder.com/150", // Placeholder image URL
        question_list: [
          "Question 1",
          "Question 2",
          "Question 3"
        ]
      }];
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
    quizId: 1,
    name: "Quiz 1",
    difficulty: "Easy",
    description: "Test your knowledge with Quiz 1",
    image: "https://via.placeholder.com/150",
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 2,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 3,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 4,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 5,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 6,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 7,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 8,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
  },
  {
    quizId: 9,
    name: "Quiz 2",
    difficulty: "Medium",
    description: "Test your knowledge with Quiz 2",
    image: "https://via.placeholder.com/150", // Placeholder image URL
    question_list: [
      "Question 1",
      "Question 2",
      "Question 3"
    ]
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
