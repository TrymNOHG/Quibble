<template>
  <div class="welcome_div">
    <div></div>
    <div class="page_info">
      <div></div>
      <div class="join_game_box">
        <input
            type="text"
            required
            v-model.trim="gamepin"
            name="GamePin"
            class="input-field"
            placeholder="Game Pin"
        />
        <div class="button_div">
          <div class="btn" aria-label="Start button">Start Game</div>
        </div>
      </div>
      <div class="login_register_text">
        <h4>
          Her kan du
          <router-link to="/login">logge deg inn</router-link>,
          eller
          <router-link to="/register">registrer deg gratis</router-link>
        </h4>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "WelcomeComponent",
  components: {},

  setup() {
    const gamepin = ""

    return {
      gamepin
    }
  }

};
</script>


<style scoped>

#text {

}

.welcome_div {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  min-height: 100vh;
}

.page_info {
  display: grid;
  grid-template-rows: 1fr 2fr 1fr;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.join_game_box {
  padding: 10%;
  border-radius: 5px;
  width: 100%;
  background: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.pin_input {
  border-radius: 5px;
}

.button_div {
  margin-top: 5%;
  display: flex;
  flex-direction: column;
}

.btn {
  color: white;
  background: #49434B;
  cursor: pointer;
  display: flex;
  border-radius: 5px;
  align-items: center;
  justify-content: center;
  transition: 0.3s;
  width: 100%;
  height: 40px;
}

.btn:hover {
  background-color: #8521b0;
  color: white;
  cursor: pointer;
}

.login_register_text {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 25%;
  grid-row: 3;
}
</style>
