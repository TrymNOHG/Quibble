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
            :placeholder="$t('placeholders.GAME_PIN')"
            @focus="hide_txt"
            @blur="show_txt"
        />
        <div class="button_div">
          <div class="btn" aria-label="Start button" @click="startGame">{{ $t('buttons.START_GAME') }}</div>
        </div>
      </div>
      <div class="login_register_text">
        <h4 v-if="isOnRoot">
          {{ $t('login_register_text.LOGIN_MESSAGE') }}
          <router-link to="/login">{{ $t('login_register_text.LOGIN') }}</router-link>,
          {{ $t('login_register_text.OR') }}
          <router-link to="/register">{{ $t('login_register_text.REGISTER') }}</router-link>
        </h4>
      </div>
    </div>
  </div>
</template>

<script>
import { useI18n } from 'vue-i18n';
import {computed} from "vue";
import {useRouter} from "vue-router";
import {ref} from "vue";

export default {
  name: "welcome_page",
  components: {},

  setup() {
    const { t } = useI18n();
    const router = useRouter();
    const gamepin = ref("");

    const isOnRoot = computed(()=>{
      return route.path === "/";
    });

    const hide_txt = () => {
      const inputField = document.querySelector('.pin_input');
      if (inputField) {
        inputField.placeholder = '';
      }
    };

    const show_txt = () => {
      const inputField = document.querySelector('.pin_input');
      if (inputField) {
        inputField.placeholder = t('placeholders.GAME_PIN');
      }
    };

    const startGame = () => {
      // Input validation
      console.log("Starting game with PIN:", gamepin.value);
      if (!gamepin.value) {
        return;
      }
      console.log("Joining game with PIN:", gamepin.value);
      router.push({ name: 'GameClient', params: { gameId: gamepin.value } });
    };

    return {
      hide_txt,
      show_txt,
      isOnRoot,
      gamepin,
      startGame
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
  height: 80%;
  min-height: 80vh;
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
  margin-top: 15%;
  grid-row: 3;
}

@media only screen and (max-width: 428px) {
  .welcome_div {
    height: auto;
  }

  .join_game_box{
    height: 150px;
    width: 300px;
  }

  .page_info {
    grid-template-rows: 1fr 2fr 1fr;
  }

  .pin_input {
    height: 40px;
  }

  .btn {
    height: 35px;
  }
}

</style>
