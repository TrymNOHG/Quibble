import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('storeUser', {

  state: () => {
    return{
      sessionToken: null,
      user: {
        userID: "",
        username: "",
        email: "",
      }
    }
  },

  actions: {
    setToken(value) {
      this.sessionToken = value
    },

    async fetchUserData() {
      await getUser()
          .then(response => {
            this.user = response
          }).catch(error  => {
            console.warn("error", error)
          })
    },

    logoutUser() {
      localStorage.removeItem("sessionToken")
      localStorage.removeItem("user")
    }
  },

  getters: {
    getUserData() {return this.user},
    isAuth() {return this.sessionToken !== null}
  },

  persist: {
    storage: sessionStorage
  }
})
