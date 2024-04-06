<template>
  <div class="players-container">
    <h2>Players Joined</h2>
    <div class="player-grid">
      <div v-for="player in players" :key="player.username" class="player-card">
        <img :src="getPictureURL(player.profilePicture)" class="player-avatar" alt="Player Profile" />
        <div class="player-name">{{ player.username }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPictureFromID } from "@/services/ImageService.js";

export default {
  props: {
    players: Array,
  },
  methods: {
    getPictureURL(id) {
      return getPictureFromID(id);
    },
  },
};
</script>

<style scoped>
.players-container {
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 100%;
  max-width: 960px; /* Adjust based on your preference */
  margin: 20px auto;
}

.player-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(100px, 1fr)); /* Responsive grid */
  gap: 20px; /* Space between cards */
}

.player-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px;
  background-color: #673AB7; /* A playful color */
  color: #fff;
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s;
}

.player-card:hover {
  transform: scale(1.05); /* A subtle hover effect */
}

.player-avatar {
  width: 80px; /* Larger avatars */
  height: 80px;
  border-radius: 50%;
  margin-bottom: 10px;
  border: 2px solid #fff; /* Add a border to make the avatar stand out */
}

.player-name {
  font-size: 1em;
}
</style>
