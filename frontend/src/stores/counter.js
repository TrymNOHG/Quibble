import { defineStore } from 'pinia'
import {
  checkSuperUser,
  getMoreQuizzes, getQuizzesByDifficulty,
  getSearchedQuizzes,
  getUser
} from "@/services/UserService.js";
import {getPictureFromUser} from "@/services/ImageService.js";

export const useUserStore = defineStore('storeUser', {

  state: () => {
    return{
      sessionToken: null,
      user: {
        userId: "",
        username: "",
        email: "",
        profilePicture : ""
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
            this.user = response.data
            getPictureFromUser('2', 'profile_pic.PNG').then(response =>{
              this.user.profilePicture = `data:${response.data.contentType};base64,${response.data.image}`;
              console.log(response)
              console.log(this.user.profilePicture)
            }).catch(e => {
              console.log(e)
            });
          }).catch(error  => {
            console.warn("error", error)
          })
    },

    logoutUser() {
      localStorage.removeItem("sessionToken")
      localStorage.removeItem("user")
      this.setToken(null)
      useQuizStore().resetCurrentQuiz()
      //TODO: invalidate token in backend.
    }
  },

  getters: {
    getUserData() {return this.user},
    isAuth() {return this.sessionToken !== null}, //TODO: should check if token is valid...
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
            answers: [],
            correctAnswers: [],
            type: ""
          },
        ],
      },
      isAuth: false,
      isEditor: false,
    }
  },

  actions: {
    async loadQuizzes(searchword, difficulty, pageIndex, numberOfQuizzes) {
      /*
      try {
        const response = await fetchQuizzes(searchword, difficulty, pageIndex, numberOfQuizzes);
        this.allQuizzes = [ ...response.content ];
        return this.allQuizzes;
      } catch (error) {
          console.error("Failed to load previous page:", error);
          pageIndex.value--;
      }
       */
    },

    async deleteCurrentQuiz() {
      /*
      TODO: axioscall
      deleteQuizById(this.currentQuiz.quizId)
      */
    },

    async editQuestion(editedQuestion){
      /*
      TODO: axioscall
      editQuestionById(this.currentQuiz.quizId, editedQuestion.id)
      */
    },

    async addQuestion(newQuestion){
      /*
      TODO: axioscall
      addQuestion(this.currentQuiz.quizId, newQuestion)
      */
    },

    async addQuiz() {
      /*
      TODO: axioscall
      addQuizById(user.id, this.currentQuiz.quizId)
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

    async addAuthor(newAuthor) {
      this.allAuthors.push({id: 4, username: newAuthor.username})
      /*
      TODO: legge til user i backend og returne den nye listen fra backend
      setNewAuthor(newAuthor)
      fetchAuthors(this.currentQuiz.quizId)
      */
      return this.allAuthors;
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
            id: 1,
            question: "What is your question?",
            answer: "John",
            answers: ["pencil", "book", "John", "quiz"],
            correctAnswers: [false, true, false, false],
            type: "multiplechoice"
          },
          {
            id: 2,
            question: "Are you 21 years old?",
            answer: "true",
            answers: ["true", "false"],
            type: "truefalse"
          },
          {
            id: 3,
            question: "What is in the center of the milky way",
            answer: "Black Hole",
            answers: ["Sun", "Earth", "Venus", "Black Hole"],
            correctAnswers: [false, true, false, false],
            type: "multiplechoice"
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

    },


    async addTag(newTag) {

    },

    async createQuiz(questions) {
      this.templateQuiz.question_list = questions.value;
      console.log(this.templateQuiz)
    },
  },
})
