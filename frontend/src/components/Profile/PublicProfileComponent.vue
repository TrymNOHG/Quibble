<template>
  <div class="profile-and-quiz-view">
    <div class="user-info">
      <img :src="user.profilePicture" alt="User's Profile Picture" class="profile-picture" />
      <h3 class="username">{{ user.username }}</h3>
    </div>

    <div class="quiz-carousel">
      <button class="nav-button prev-button" @click="scroll(-1)" @mouseenter="hover=true" @mouseleave="hover=false">&#10094;</button>
      <div class="carousel-slides">
        <div class="carousel-slide" v-for="(quiz, index) in visibleQuizzes" :key="quiz.id"
             :class="{'apply-gradient-left': applyGradients && index === 0, 'apply-gradient-right': applyGradients && index === visibleQuizzes.length - 1}">
          <QuizCard :quiz="quiz" :class="{ 'is-focused': focusedIndex === index }" />
        </div>
      </div>
      <button class="nav-button next-button" @click="scroll(1)" @mouseenter="hover=true" @mouseleave="hover=false">&#10095;</button>
    </div>
  </div>
</template>


<script>
import {ref, computed, onMounted, onUnmounted, watch, toRefs} from 'vue';
import QuizCard from '@/components/BasicComponents/one_quiz_rectangle.vue';

export default {
  components: {
    QuizCard,
  },
  props: {
    user: {
      type: Object,
      required: true,
      default: () => ({
        username: 'John Doe',
        profilePicture: 'https://placehold.co/600x400'
      })

    },
    quizzes: {
      type: Array,
      required: true,
      default: () => [
        {
          id: 1,
          title: 'Quiz 1',
          description: 'This is the first quiz',
          imageUrl: 'https://placehold.co/600x400',
          question_list: [
            {
              question: 'What is the capital of France?',
              options: ['Paris', 'London', 'Berlin', 'Madrid'],
              correctAnswer: 'Paris'
            },
            {
              question: 'What is the capital of Spain?',
              options: ['Paris', 'London', 'Berlin', 'Madrid'],
              correctAnswer: 'Madrid'
            }
          ]
        }

      ]
    }
  },

  setup(props) {
    const user = toRefs(props).user;

    const quizzes = toRefs(props).quizzes;
    const windowWidth = ref(window.innerWidth);
    const currentIndex = ref(0);
    const focusedIndex = ref(null);


    // Adjust this function based on your responsive design breakpoints
    const calculateVisibleCount = () => {
      const availableQuizzes = quizzes.value.length; // Get the total number of quizzes
      // Adjust this function based on your responsive design breakpoints
      if (windowWidth.value >= 2560) return Math.min(availableQuizzes, 6); // example for extra extra large desktop
      if (windowWidth.value >= 1920) return Math.min(availableQuizzes, 5); // example for extra large desktop
      if (windowWidth.value >= 1440) return Math.min(availableQuizzes, 4); // example for large desktop
      if (windowWidth.value >= 1024) return Math.min(availableQuizzes, 3); // example for desktop
      if (windowWidth.value >= 768 ) return Math.min(availableQuizzes, 2); // example for tablet
      return Math.min(availableQuizzes, 1); // default for mobile
    };


    const visibleQuizzes = computed(() => {
      const visibleCount = calculateVisibleCount();
      let quizzesToShow = [];
      for (let i = 0; i < visibleCount; i++) {
        let index = (currentIndex.value + i - Math.floor(visibleCount / 2) + quizzes.value.length) % quizzes.value.length;
        quizzesToShow.push(quizzes.value[index]);
      }
      return quizzesToShow;
    });

    const updateWindowWidth = () => {
      windowWidth.value = window.innerWidth;
    };

    onMounted(() => {
      window.addEventListener('resize', updateWindowWidth);
    });

    onUnmounted(() => {
      window.removeEventListener('resize', updateWindowWidth);
    });

    const applyGradients = computed(() => {
      // Determine if gradients should be applied based on the number of visible quizzes
      return visibleQuizzes.value.length > 2;
    });

    const scroll = (direction) => {
      currentIndex.value += direction;
    };



    return {
      user,
      visibleQuizzes,
      currentIndex,
      scroll,
      focusedIndex,
      applyGradients
    };
  },
};
</script>




<style scoped>
.profile-and-quiz-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
}

.user-info {
  margin-bottom: 2rem;
  text-align: center;
}

.profile-picture {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #007bff;
  transition: transform 0.3s ease;
}

.username {
  margin-top: 1rem;
  font-size: 1.2rem;
  color: #333;
}

.quiz-carousel {
  position: relative;
  overflow: hidden;
}

.carousel-slides {
  display: flex;
  transition: transform 0.5s ease-out; /* Smooth transition for sliding effect */
}






.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: #fff;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
  z-index: 2; /* Ensure buttons are above the slides */
}

.nav-button:hover {
  background-color: #e7e7e7;
}

.carousel-slides {
  display: flex;
  flex-wrap: nowrap; /* Prevent wrapping */
  justify-content: center; /* Center slides */
  transition: transform 0.5s ease-out;
}


.carousel-slide {
  position: relative;
  width: 300px;
  display: flex;

  margin: 0 20px; /* Add some space between slides */
  align-items: center;
  justify-content: start; /* Align children to the start if you are translating them */
  transition: opacity 0.5s ease, transform 0.5s ease; /* Transition for focusing effect */
}

.carousel-slide {
  -webkit-mask-image: none;
  mask-image: none;
}

/* Add gradient to the left side of the first slide */
.apply-gradient-left {
  -webkit-mask-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,1));
  mask-image: linear-gradient(to right, rgba(0,0,0,0), rgba(0,0,0,1));
}

/* Add gradient to the right side of the last slide */
.apply-gradient-right {
  -webkit-mask-image: linear-gradient(to left, rgba(0,0,0,0), rgba(0,0,0,1));
  mask-image: linear-gradient(to left, rgba(0,0,0,0), rgba(0,0,0,1));
}

.prev-button {
  left: 10px; /* Adjust based on your design */
}

.next-button {
  right: 10px; /* Adjust based on your design */
}
</style>
