import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedState from "pinia-plugin-persistedstate"

import App from './App.vue'
import './assets/main.css'
import router from "../src/router";


/* Icons imports */
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

/* add icons to the library */
import {
    faPuzzlePiece,
    faHouse,
    faUserCircle, faCirclePlus, faMagnifyingGlass, faBars, faPenToSquare, faTrash, faAdd,
} from "@fortawesome/free-solid-svg-icons";

library.add(
    faHouse,
    faUserCircle,
    faPuzzlePiece,
    faCirclePlus,
    faMagnifyingGlass,
    faBars,
    faTrash,
    faPenToSquare,
    faAdd,
    faCirclePlus
)


/* Imports fr multiple languages */
import i18n from "@/lang/i18n";




const pinia = createPinia();

pinia.use(piniaPluginPersistedState);

createApp(App)
    .use(pinia)
    .use(i18n)
    .use(router)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount("#app");