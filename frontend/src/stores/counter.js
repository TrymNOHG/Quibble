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


export const useQuizStore = defineStore('storeQuiz', {

  state: () => {
    return {
      allQuiz: [],
      allAuthors: [{
          id: 1,
          username: 'Author 1'},
        {
          id: 2,
          username: 'Author 2'},
        {
          id: 3,
          username: 'Author 3'}],

      currentQuiz: {
        QuizId: null,
        Name: "",
        Difficulty: "",
        Description: "",
        Image: "",
        question_list: [
          {
            id: null,
            question: "",
            answer: "",
            type: ""
          },
        ],
      },
      isAuth: false,
      isEditor: false,
    }
  },

  actions: {
    async deleteCurrentQuiz() {
      /*
      TODO: axioscall
      deleteQuizById(this.currentQuiz.quizId)
      */
    },


    async deleteQuestion(question_id) {
      for (let index = 0; index < this.currentQuiz.question_list.length; index++) {
        if (question_id === this.currentQuiz.question_list[index].id) {
          this.currentQuiz.question_list.splice(index, 1);
          /*
          TODO: fjerne q i backend
          response = deleteQuestion(q.id)
          this.currentQuiz.Questions = response;
           */
          break;
        }
      }
      return this.currentQuiz.question_list;
    },

    async deleteAuth(auth) {
      for (let index = 0; index < this.allAuthors.length; index++) {
        if (auth.id === this.allAuthors[index].id) {
          this.allAuthors.splice(index, 1);
          /*
          TODO: fjerne auth i backend
          response = removeAuth(author.username // author-id)
          this.allAuthors = response;
           */
          break;
        }
      }
      console.log("KL")
    },

    async setCurrentQuizById(quiz) {
      console.log(quiz)
      this.currentQuiz = quiz;
      this.isAuth = true;
      this.isEditor = true;
      /*
      TODO: Sjekke opp mot backend
      isAuth og isEditor burde sjekkes opp mot axioscall til backend
       */
      return this.currentQuiz;
    },

    async searchQuizzes(searchword) {
      this.allQuiz = getSearchedQuizzes(searchword);
      return this.allQuiz;
    },

    async addAuthor(newAuthor) {
      this.allAuthors.push({id: 4, username: newAuthor.username})
      /*
      TODO: legge til user i backend og returne den nye listen fra backend
      setNewAuthor(newAuthor)
      fetchAuthors(this.currentQuiz.quizId)
      */
      return this.allAuthors;
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
      this.isAuth = false;
      this.isEditor = false;
    },
  },
})

export const useQuizCreateStore = defineStore('storeQuizCreate', {
  state: () => {
    return {
      templateQuiz: {
        QuizId: null,
        Name: "TemplateQuiz",
        Difficulty: "Easy",
        Description: "Template quiz, change the quiz as wanted",
        Image: "https://via.placeholder.com/150",
        question_list: [
          {
            id: null,
            question: "What is your name?",
            answer: "John",
            answers: ["pencil", "book", "John", "quiz"],
            type: "MultipleChoice"
          },
          {
            id: null,
            question: "Are you 21 years old?",
            answer: "Yes",
            answers: ["Yes", "No"],
            type: "TrueFalse"
          },
          {
            id: null,
            question: "What is in the center of the milky way",
            answer: "Black Hole",
            answers: ["Sun", "Earth", "Venus", "Black Hole"],
            type: "MultipleChoice"
          },
        ],
      },

      template_tags:{
        tags: [
            {
              tag_desc: "disney",
              type: "Category"
            },
            {
              tag_desc: "movies",
              type: "Keyword"
            }
        ]
      }
    }
  },

  actions: {
    async deleteTag(tag) {
      for (let index = 0; index < this.allAuthors.length; index++) {
        if (auth.id === this.allAuthors[index].id) {
          this.allAuthors.splice(index, 1);
          /*
          TODO: fjerne auth i backend
          response = removeAuth(author.username // author-id)
          this.allAuthors = response;
           */
          break;
        }
      }
      console.log("KL")
    },


    async addTag(newTag) {
      this.allAuthors.push({id: 4, username: newAuthor.username})
      /*
      TODO: legge til user i backend og returne den nye listen fra backend
      setNewAuthor(newAuthor)
      fetchAuthors(this.currentQuiz.quizId)
      */
      return this.allAuthors;
    },

  },

})
