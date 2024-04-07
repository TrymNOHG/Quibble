<template>
  <header v-if="!isOnRoot">
    <router-link to="/home" aria-label="Go to Home page">
      <img
          id="logo"
          alt="Logo, and routerlink to homepage"
          src="@/assets/images/logov1.png"/>
    </router-link>
    <router-link class="header_text" to="/home">
      <h1 class="header_text">{{ headerText }}</h1>
    </router-link>
    <nav>
      <ul>
        <router-link v-show="isAuthenticated" to="/home">
          <li>
              <font-awesome-icon class="icon" icon="fa-solid fa-home"/>
          </li>
        </router-link>
        <router-link to="/">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-play" />
          </li>
        </router-link>
        <router-link  v-show="isAuthenticated" to="/myquiz">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-puzzle-piece"/>
          </li>
        </router-link>
        <router-link v-show="isAuthenticated" to="/create">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-circle-plus" />
          </li>
        </router-link>
        <router-link to="/profile">
          <li>
            <font-awesome-icon v-if="isAuthenticated" class="icon" icon="fa-solid fa-circle-user" />
            <font-awesome-icon v-else class="icon" icon="fa-solid fa-right-to-bracket" />
          </li>
        </router-link>
        <div class="language" @click="changeLang()">{{ language }}</div>
      </ul>
    </nav>
  </header>
  <RouterView/>
</template>

<script>
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";
import {ref, computed} from "vue";
import {useI18n} from "vue-i18n";
import {useRoute} from "vue-router";
import {useUserStore} from "@/stores/counter.js";

export default {
  components: { TheWelcomeComponent },

  setup() {
   let language = ref("NO");
   const { locale } = useI18n();
   const route = useRoute();
   const isOnRoot = computed(()=>{
     return route.path === "/";
   });

    const headerText = computed(() => {
      if (route.path === "/home"){return "Home"}
      if (route.path === "/myquiz"){return "My Quizzes"}
      if (route.path === "/quiz"){return "Quiz"}
      if (route.path === "/profile"){return "Profile"}
      if (route.path === "/create"){return "Create"}

    });

    const changeLang =  () => {
     if (language.value === "EN") {
       locale.value = "NO";
       language.value = "NO";
     }
     else {
       language.value = "EN";
       locale.value = "EN";
     }
   };

    const store = useUserStore();
    const isAuthenticated = computed(() => store.isAuth);

   return {
     language,
     changeLang,
     headerText,
     isOnRoot,
     isAuthenticated
   }
  }
}
</script>

<style scoped>
header {
  background-color: #8521b0;
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  align-content: center;
  justify-content: space-between;
}

.language {
  cursor: pointer;
  width: 20px;
  height: 20px;
  color: white;
  top: 15px;
}

.language {
  margin-left: 25px;
  margin-right: 25px;
}

.language:hover {
  scale: 1.2;
}

#logo {
  width: 100px;
  height: 100px;
}

.header_text {
  color: white;
  font-weight: bold;
  margin-left: 15%;
  width: max-content;
  text-decoration: none !important;
}


nav ul {
  list-style: none;
  display: flex;
}

li {
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px;
  background-color: rgba(19, 155, 250, 0.88);
  width: 60px;
  height: 60px;
  border-radius: 10px;
}

li:hover {
  filter: invert(100%) sepia(50%) saturate(100%) hue-rotate(180deg) brightness(250%) contrast(100%);
  transform: translateY(-5%);
  cursor: pointer;
}

.icon {
  scale: 2;
  color: white;
}

@media only screen and (max-width: 428px) {
  header {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background-color: #8521b0;
    z-index: 1;
  }

  .language {
    margin-left: 0px;
    margin-right: 0px;
  }

  nav {
    display: flex;
    justify-content: space-evenly;
    width: 100%;
    padding: 5px;
  }

  .header_text {
    display: none;
  }

  #logo {
    display: none;
  }

  ul{
    padding: 0;
  }

  li {
    padding: 8px;
    width: 50px;
    height: 50px;
  }
}

</style>
