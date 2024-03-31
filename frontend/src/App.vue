<template>
  <header>
    <router-link to="/home" aria-label="Go to Home page">
      <img
          id="logo"
          alt="Logo, and routerlink to homepage"
          src="@/assets/images/logov1.png"/>
    </router-link>
    <router-link class="header_text" to="/home">
      <h1 class="header_text">Quibble</h1>
    </router-link>
    <nav>
      <ul>
        <router-link to="/home">
          <li>

              <font-awesome-icon class="icon" icon="fa-solid fa-home"/>
          </li>
        </router-link>
        <router-link to="/">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-play" />
          </li>
        </router-link>
        <router-link to="/quiz">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-puzzle-piece"/>
          </li>
        </router-link>
        <router-link to="/create">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-circle-plus" />
          </li>
        </router-link>
<!--        <router-link to="/myquiz">-->
<!--          <li>-->
<!--            <font-awesome-icon class="icon" icon="fa-solid fa-circle-plus" />-->
<!--          </li>-->
<!--        </router-link>-->
        <router-link to="/profile">
          <li>
            <font-awesome-icon class="icon" icon="fa-solid fa-circle-user" />
          </li>
        </router-link>
      </ul>
    </nav>
  </header>
  <RouterView/>
</template>

<script>
import TheWelcomeComponent from "@/components/TheWelcomeComponent.vue";

export default {
  components: { TheWelcomeComponent },
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

#logo {
  width: 100px;
  height: 100px;
}

.header_text {
  color: white;
  font-weight: bold;
  margin-left: 15%;
  text-decoration: none !important;
}


nav ul {
  list-style: none;
  display: flex;
}

li {
  color: white;
  display: flex; /* Ensure flexbox is being used */
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
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

  nav {
    display: flex;
    justify-content: space-evenly;
    width: 100%;
    padding: 10px;
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
