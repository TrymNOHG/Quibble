import { defineStore } from 'pinia'
import {
  checkSuperUser,
  getMoreQuizzes, getQuizzesByDifficulty,
  getSearchedQuizzes,
  getUser
} from "@/services/UserService.js";

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
      useQuizStore().resetCurrentQuiz()
    }
  },

  getters: {
    getUserData() {return this.user},
    isAuth() {return this.sessionToken !== null},
    getToken() {return this.sessionToken}
  },

  persist: {
    storage: sessionStorage
  }
})


export const useQuizStore = defineStore('storeUser', {

  state: () => {
    return {
      allQuiz: [],
      currentQuiz: {
        QuizId: null,
        Name: "",
        Difficulty: "",
        Description: "",
        Image: "",
        Questions: [],
      },
      isSuperUser: false
    }
  },

  actions: {
    async setCurrentQuizById(quiz) {
      this.currentQuiz = quiz;
      console.log(this.currentQuiz)
      return this.currentQuiz;
    },

    async searchQuizzes(searchword) {
      this.allQuiz = getSearchedQuizzes(searchword);
      return this.allQuiz;
    },


    async setAllQuizzes(difficulty) {
      this.allQuiz = getQuizzesByDifficulty(difficulty);
      return this.allQuiz;
    },

    async checkSuperUser(quizId) {
      try {
        const response = await checkSuperUser(quizId);
        return response.data
      } catch (e) {
        console.error(e)
      }
    },

    async fetchMoreQuizzes(diff) {
      await getMoreQuizzes(diff).then(response => {
        this.newQuizzes = []
        this.newQuizzes = response.data;
      })
      return this.newQuizzes;
    },

    resetCurrentQuiz() {
      this.currentQuiz = null
      this.isSuperUser = false;
    },
  },
})

