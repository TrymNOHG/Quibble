<template>
  <div class="score-container">
    <h2>{{ playerScoresTitle }}</h2>    <!-- Countdown Timer Display -->
    <div v-if="!gameEnded" class="countdown-timer">{{timetranslation}} {{ countdown }}</div>
    <div v-else class="fireworks">
      <div class="firework"></div>
      <div class="firework"></div>
      <div class="firework"></div>
    </div>
    <div class="score-list">
      <div v-for="(player, index) in players" :key="player.username" class="score-item">
        <span class="player-number">{{ index + 1 }}</span>
        <img :src="getPictureURL(player.imageId)" class="player-image" alt="Player Profile" />
        <span class="player-name">{{ player.username }}</span>
        <span class="player-score">{{ player.score }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {getPictureFromID} from "@/services/ImageService.js";
import {useI18n} from "vue-i18n";
import {computed} from "vue";


export default {
  props: {
    players: Array,
    gameEnded: Boolean, // New prop to indicate if the game has ended
  },
  data() {
    return {
      countdown: 5, // Initialize countdown duration
    };
  },
  methods: {
    startCountdown() {
      let intervalId = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown -= 1;
        } else {
          clearInterval(intervalId);
          // Optional: Emit an event when countdown ends to trigger the next action
          this.$emit('countdownEnded');
        }
      }, 1000);
    },
    getPictureURL(id) {
      return getPictureFromID(id);
    },
  },
  mounted() {
    if (!this.gameEnded) {
      this.startCountdown();
    }
  },
  setup() {
    const {t} = useI18n();

    const playerScoresTitle = computed(() => t('quiz_client.playerScores'));
    const timetranslation = computed(() => t('quiz_client.timeLeft'));
    return {t,
    playerScoresTitle,
    timetranslation,
    };
  },
}
</script>

<style scoped>
/* Adjusted styles */
.score-container {
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 80%;
  max-width: 600px;
  margin: auto;
}

.countdown-timer {
  margin-bottom: 20px;
  font-size: 1.5em;
  color: #CC00FF; /* Styling to match the theme */
}
.score-container {
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 80%; /* Adjust the width as needed */
  max-width: 600px; /* Maximum width */
  margin: auto; /* Center the score container */
}

.score-list {
  margin: 0;
  padding: 0;
}

.score-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #CC00FF;
  color: #fff;
  padding: 15px;
  margin: 10px 0;
  border-radius: 5px;
  transition: transform 0.2s;
}

.player-number {
  font-size: 2em; /* Increase font size */
  font-weight: bold;
  margin-right: 10px; /* Add some margin between the number and the image */
  flex-shrink: 0;
}

.player-image {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  order: 2; /* Use order to control the layout sequence */
}

.player-info {
  display: flex;
  justify-content: center;
  flex-grow: 1;
}

.player-name {
  font-size: 1.1em;
  text-align: center;
  flex-grow: 1;
  order: 3; /* Continue with the ordering */
}

.player-score {
  font-size: 1.1em;
  font-weight: bold;
  flex-shrink: 0;
  order: 4; /* This will be the last item */
}

/*fireworks taken from https://alvaromontoro.com/blog/68002/creating-a-firework-effect-with-css*/

@keyframes firework {
  0% { transform: translate(var(--x), var(--initialY)); width: var(--initialSize); opacity: 1; }
  50% { width: 0.5vmin; opacity: 1; }
  100% { width: var(--finalSize); opacity: 0; }
}

/* @keyframes fireworkPseudo {
  0% { transform: translate(-50%, -50%); width: var(--initialSize); opacity: 1; }
  50% { width: 0.5vmin; opacity: 1; }
  100% { width: var(--finalSize); opacity: 0; }
}
 */
.firework,
.firework::before,
.firework::after
{
  --initialSize: 0.5vmin;
  --finalSize: 45vmin;
  --particleSize: 0.2vmin;
  --color1: yellow;
  --color2: khaki;
  --color3: white;
  --color4: lime;
  --color5: gold;
  --color6: mediumseagreen;
  --y: -30vmin;
  --x: -50%;
  --initialY: 60vmin;
  content: "";
  animation: firework 2s infinite;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, var(--y));
  width: var(--initialSize);
  aspect-ratio: 1;
  background:
    /*
    radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 0% 0%,
    radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 100% 0%,
    radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 100% 100%,
    radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 0% 100%,
    */

      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 50% 0%,
      radial-gradient(circle, var(--color2) var(--particleSize), #0000 0) 100% 50%,
      radial-gradient(circle, var(--color3) var(--particleSize), #0000 0) 50% 100%,
      radial-gradient(circle, var(--color4) var(--particleSize), #0000 0) 0% 50%,

        /* bottom right */
      radial-gradient(circle, var(--color5) var(--particleSize), #0000 0) 80% 90%,
      radial-gradient(circle, var(--color6) var(--particleSize), #0000 0) 95% 90%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 90% 70%,
      radial-gradient(circle, var(--color2) var(--particleSize), #0000 0) 100% 60%,
      radial-gradient(circle, var(--color3) var(--particleSize), #0000 0) 55% 80%,
      radial-gradient(circle, var(--color4) var(--particleSize), #0000 0) 70% 77%,

        /* bottom left */
      radial-gradient(circle, var(--color5) var(--particleSize), #0000 0) 22% 90%,
      radial-gradient(circle, var(--color6) var(--particleSize), #0000 0) 45% 90%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 33% 70%,
      radial-gradient(circle, var(--color2) var(--particleSize), #0000 0) 10% 60%,
      radial-gradient(circle, var(--color3) var(--particleSize), #0000 0) 31% 80%,
      radial-gradient(circle, var(--color4) var(--particleSize), #0000 0) 28% 77%,
      radial-gradient(circle, var(--color5) var(--particleSize), #0000 0) 13% 72%,

        /* top left */
      radial-gradient(circle, var(--color6) var(--particleSize), #0000 0) 80% 10%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 95% 14%,
      radial-gradient(circle, var(--color2) var(--particleSize), #0000 0) 90% 23%,
      radial-gradient(circle, var(--color3) var(--particleSize), #0000 0) 100% 43%,
      radial-gradient(circle, var(--color4) var(--particleSize), #0000 0) 85% 27%,
      radial-gradient(circle, var(--color5) var(--particleSize), #0000 0) 77% 37%,
      radial-gradient(circle, var(--color6) var(--particleSize), #0000 0) 60% 7%,

        /* top right */
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 22% 14%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 45% 20%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 33% 34%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 10% 29%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 31% 37%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 28% 7%,
      radial-gradient(circle, var(--color1) var(--particleSize), #0000 0) 13% 42%
;
  background-size: var(--initialSize) var(--initialSize);
  background-repeat: no-repeat;
}

.firework::before {
  --x: -50%;
  --y: -50%;
  --initialY: -50%;
  /*   transform: translate(-20vmin, -2vmin) rotate(40deg) scale(1.3) rotateY(40deg); */
  transform: translate(-50%, -50%) rotate(40deg) scale(1.3) rotateY(40deg);
  /*   animation: fireworkPseudo 2s infinite; */
}

.firework::after {
  --x: -50%;
  --y: -50%;
  --initialY: -50%;
  /*   transform: translate(44vmin, -50%) rotate(170deg) scale(1.15) rotateY(-30deg); */
  transform: translate(-50%, -50%) rotate(170deg) scale(1.15) rotateY(-30deg);
  /*   animation: fireworkPseudo 2s infinite; */
}

.firework:nth-child(2) {
  --x: 30vmin;
}

.firework:nth-child(2),
.firework:nth-child(2)::before,
.firework:nth-child(2)::after {
  --color1: pink;
  --color2: violet;
  --color3: fuchsia;
  --color4: orchid;
  --color5: plum;
  --color6: lavender;
  --finalSize: 40vmin;
  left: 30%;
  top: 60%;
  animation-delay: -0.25s;
}

.firework:nth-child(3) {
  --x: -30vmin;
  --y: -50vmin;
}

.firework:nth-child(3),
.firework:nth-child(3)::before,
.firework:nth-child(3)::after {
  --color1: cyan;
  --color2: lightcyan;
  --color3: lightblue;
  --color4: PaleTurquoise;
  --color5: SkyBlue;
  --color6: lavender;
  --finalSize: 35vmin;
  left: 70%;
  top: 60%;
  animation-delay: -0.4s;
}



</style>
