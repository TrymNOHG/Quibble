<template>
  <div class="score-container single-player">
    <h2>Your Score</h2>
    <div class="score-item">
      <img :src="getPictureURL(player.userId)" class="player-image" alt="Player Profile" />
      <div class="player-details">
        <span class="player-name">{{ player.username }}</span>
        <span class="player-score">{{ score }}</span>
        <!-- Display Countdown Timer -->
        <div class="countdown-timer">{{ countdown }}</div>
      </div>
    </div>
  </div>
</template>

<script>

import {getPictureFromID} from "@/services/ImageService.js";

export default {
  props: {
    player: {
      type: Object,
      required: true
    },
    score: {
      type: Number,
      required: true
    }
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
          // Optional: Emit an event when countdown ends
          this.$emit('countdownEnded');
        }
      }, 1000);
    },
    getPictureURL(id) {
      return getPictureFromID(id);
    },
  },
  mounted() {
    this.startCountdown(); // Start countdown when component is mounted
  }
}
</script>

<style scoped>
/* Existing styles */
.countdown-timer {
  margin-top: 20px;
  font-size: 1.5em;
  color: #CC00FF; /* Styling to match the theme */
}

.score-container {
  background: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 80%;
  max-width: 600px;
  margin: auto;
}

.single-player h2 {
  margin-bottom: 20px;
  color: #CC00FF;
  text-transform: uppercase;
}

.score-item {
  display: flex;
  align-items: center;
  justify-content: center; /* Center items for a cleaner look */
  background-color: #CC00FF;
  color: #fff;
  padding: 20px;
  margin: 10px 0;
  border-radius: 5px;
}

.player-image {
  width: 120px; /* Increased size for focus */
  height: 120px;
  border-radius: 50%;
  border: 4px solid #fff;
  margin-right: 20px; /* Increased spacing */
}

.player-details {
  display: flex;
  flex-direction: column;
  align-items: center; /* Center-aligned for single-player focus */
}

.player-name {
  font-size: 2em;
  margin-bottom: 10px; /* Increased spacing */
}

.player-score {
  font-size: 2.5em; /* Larger font size for emphasis */
  font-weight: bold;
}
</style>
