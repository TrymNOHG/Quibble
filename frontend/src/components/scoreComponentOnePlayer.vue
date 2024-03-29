<template>
  <div class="score-container single-player">
    <h2>Your Score</h2>
    <div class="score-item">
      <span class="player-number">{{ player.rank }}</span>
      <img :src="player.image" class="player-image" alt="Player Profile" />
      <div class="player-details">
        <span class="player-name">{{ player.name }}</span>
        <span class="player-score">{{ player.score }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    player: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>
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
  color: #CC00FF; /* Using the same color for consistency */
  text-transform: uppercase; /* Making the header stand out */
}

.score-item {
  display: flex;
  align-items: center;
  background-color: #CC00FF;
  color: #fff;
  padding: 20px;
  margin: 10px 0;
  border-radius: 5px;
}

.player-number {
  font-size: 3em; /* Large font size for ranking */
  font-weight: bold;
  margin-right: 15px;
}

.player-image {
  width: 100px; /* Slightly larger image for single player */
  height: 100px;
  border-radius: 50%;
  border: 4px solid #fff; /* White border around the image */
  margin-right: 15px;
}

.player-details {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.player-name {
  font-size: 1.5em; /* Larger font size for better readability */
  margin-bottom: 5px;
}

.player-score {
  font-size: 2em; /* Even larger font size for the score */
  font-weight: bold;
}
</style>
