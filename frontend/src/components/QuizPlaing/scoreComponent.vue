<template>
  <div class="score-container">
    <h2>Player Scores</h2>
    <div class="score-list">
      <div v-for="(player, index) in players" :key="player.name" class="score-item">
        <span class="player-number">{{ index + 1 }}</span>
        <img :src="player.image" class="player-image" alt="Player Profile" />
        <span class="player-name">{{ player.name }}</span>
        <span class="player-score">{{ player.score }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    players: Array
  }
}
</script>


<style scoped>
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

</style>
