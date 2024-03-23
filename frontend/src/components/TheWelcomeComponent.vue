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
            class="pin_input"
            placeholder="GAME PIN"
            @focus="hide_txt"
            @blur="show_txt"
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
  name: "welcome_page",
  components: {},

  setup() {
    const gamepin = ""

    const hide_txt = () => {
      const inputField = document.querySelector('.pin_input');
      if (inputField) {
        inputField.placeholder = '';
      }
    };

    const show_txt = () => {
      const inputField = document.querySelector('.pin_input');
      if (inputField) {
        inputField.placeholder = 'GAME PIN';
      }
    };

    return {
      hide_txt,
      show_txt,
      gamepin
    }
  }

};
</script>


<style scoped>
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

.pin_input::placeholder {
  font-size: 120%;
  color: #8521b0;
  opacity: 75%;
}

.join_game_box {
  padding: 10%;
  border-radius: 5px;
  background: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.pin_input {
  border-radius: 5px;
  width: 100%;
  height: 50px;
  text-align: center;
  color: #8521b0;
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
