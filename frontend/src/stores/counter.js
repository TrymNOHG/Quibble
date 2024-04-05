import { defineStore } from 'pinia'
import {
  fetchUserByUsername,
  getUser
} from "@/services/UserService.js";
import {getPictureFromUser} from "@/services/ImageService.js";

import {
  addCategory,
  addCollaborator, addKeyword,
  addQuestion,
  createQuiz,
  deleteQuestionById,
  deleteQuizById,
  fetchQuizzes,
  patchQuestion,
  removeCollaborator,
  updateQuiz
} from "@/services/QuizService.js"

export const useUserStore = defineStore('storeUser', {

  state: () => {
    return{
      sessionToken: null,
      user: {
        userId: "",
        username: "",
        email: "",
        profilePicture : "",
        showActivity: false,
        showFeedback: false
      }
    }
  },

  actions: {
    setToken(value) {
      this.sessionToken = value
    },
    setShowActivity(value) {
      this.user.showActivity = value
    },
    setShowFeedback(value) {
      this.user.showFeedback = value
    },

    async fetchUserData() {
      await getUser()
          .then(response => {
            this.user = response.data
            getPictureFromUser('2', response.data.profilePicture).then(response =>{
              this.user.profilePicture = `data:${response.data.contentType};base64,${response.data.image}`;
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

      currentQuiz: {
        quizId: null,
        quizName: "",
        quizDifficulty: "",
        quizDescription: "",
        adminId: null,
        feedback: [],
        collaborators: Set,
        categories: [],
        questions: [
          {
            quizId: null,
            questionId: null,
            question: "",
            answer: "",
            type: "",
            choices: [],
          },
        ],
        keywords: [],
        Image: "",
      },

      category_list: ["Movies","School","Sport","Geography"]
    }
  },

  actions: {
    //searchword, difficulty, pageIndex,
    //TODO: search, diff, page

    async loadQuizzes(page, size) {
      try {
        const response = await fetchQuizzes(page, size);
        console.log(response)
        this.allQuizzes = [ ...response ];
        return this.allQuizzes;
      } catch (error) {
          console.error("Failed to load previous page:", error);
      }
    },

    async filterAuthor(searchQuery) {
      try {
        console.log(searchQuery)
        const response = await fetchUserByUsername(searchQuery);
        console.log(response)
        return response;
      } catch (error) {
        console.error("Failed to load previous page:", error);
      }
    },

    isAdmin(adminId) {
      return this.currentQuiz.adminId === useUserStore().user.userId;
    },

    async deleteCurrentQuiz() {
      await deleteQuizById(this.currentQuiz.quizId)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async editQuestion(editedQuestion){
      console.log(editedQuestion.quizId)
      console.log(this.currentQuiz.quizId)
      const editQuestionDTO = {
        "quizId": editedQuestion.quizId,
        "questionId": editedQuestion.questionId,
        "question": editedQuestion.question,
        "type": editedQuestion.type,
        "choices": editedQuestion.choices
      }

      await patchQuestion(editQuestionDTO)
          .then(responses => {
            console.log(responses);
            return responses;
          }).catch(error => {
            console.warn("error", error);
          });
    },

    async addQuestion(newQuestion){
      const questionCreateDTO = {
        "quizId": this.currentQuiz.quizId,
        "question": newQuestion.question,
        "answer": newQuestion.answer,
        "type": newQuestion.type,
        "choices": newQuestion.choices
      };

      await addQuestion(questionCreateDTO)
          .then(responses => {
            console.log(responses);
          }).catch(error => {
            console.warn("error", error);
          });
    },

    async addQuiz() {
      /*
      TODO: axioscall
      addQuizById(user.id, this.currentQuiz.quizId)
      */
    },

    async deleteQuestion(question_id) {
      console.log(question_id)
      await deleteQuestionById(question_id)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async deleteAuth(auth) {
      try {
        const response = await removeCollaborator(auth.quizAuthorId);
        const index = this.currentQuiz.collaborators.findIndex(author => author.quizAuthorId === auth.quizAuthorId);
        if (index !== -1) {
          this.currentQuiz.collaborators.splice(index, 1);
        } else {
          console.warn("Author not found in collaborators array");
        }
      } catch (error) {
        console.error("Error deleting author:", error);
      }
    },


    async setCurrentQuizById(quiz) {
      console.log(quiz)
      this.currentQuiz = quiz;
      if (useUserStore().user.userId === this.currentQuiz.QuizId){
        this.isAuth = true;
        this.isEditor = true;
      }
      return this.currentQuiz;
    },

    async updateQuiz(quizUpdateDTO) {

      console.log(quizUpdateDTO)
      await updateQuiz(quizUpdateDTO)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
    },

    async addAuthor(author) {
      const quizAuthorDTO = {
        userId: author.userId,
        quizId: this.currentQuiz.quizId
      };
      await addCollaborator(quizAuthorDTO)
          .then(response => {
            console.log(response)
            this.currentQuiz.collaborators.push(response)
          }).catch(error => {
            console.warn("error", error)
          })

      return this.currentQuiz.collaborators;
    },

    resetCurrentQuiz() {
      this.currentQuiz = null;
      this.isAuth = false;
      this.isEditor = false;
    },
  },

  persist: {
    storage: sessionStorage
  }
})

export const useQuizCreateStore = defineStore('storeQuizCreate', {
  state: () => {
    return {
      templateQuiz: {
        QuizId: null,
        quizName: "TemplateQuiz",
        quizDifficulty: "Easy",
        quizDescription: "Template quiz, change the quiz as wanted",
        admin_id: null,
        feedbacks: [],
        collaborators: [],
        categories: [],
        questions: [
          {
            quizId: null,
            question: "What is your question?",
            answer: "John",
            type: "multiple_choice",
            choices: [
              { alternative: "pencil", isCorrect: false},
              { alternative: "book", isCorrect: false},
              { alternative: "John", isCorrect: true},
              { alternative: "quiz", isCorrect: false}
            ]
          },
          {
            quizId: null,
            question: "Are you 21 years old?",
            answer: "true",
            type: "true_false",
            choices: [
              {alternative: "true", isCorrect: true},
              {alternative: "false", isCorrect: false}
            ]
          },
          {
            quizId: null,
            question: "What is in the center of the Milky Way?",
            answer: "Black Hole",
            type: "multiple_choice",
            choices: [
              { alternative: "Sun", isCorrect: false },
              { alternative: "Earth", isCorrect: false },
              { alternative: "Venus", isCorrect: false },
              { alternative: "Black Hole", isCorrect: true }
            ]
          },
        ],
        keywords: [],
        Image: "https://via.placeholder.com/150",
      }
    }
  },

  actions: {
    async createQuiz(questions) {
      let createdQuiz = null;
      this.templateQuiz.questions = questions.value;

      await createQuiz(this.templateQuiz.quizName)
          .then(response => {
            console.log(response);
            createdQuiz = response;
          }).catch(error => {
            console.warn("Error creating quiz:", error);
          });

      const quizUpdateDTO = {
        "quizId": createdQuiz.quizId,
        "newName": createdQuiz.quizName,
        "newDescription": this.templateQuiz.quizDescription,
        "difficulty": this.templateQuiz.quizDifficulty.toUpperCase(),
      };

      const addQuestionPromises = [];

      console.log(this.templateQuiz.questions);
      this.templateQuiz.questions.forEach(question => {
        const questionCreateDTO = {
          "quizId": createdQuiz.quizId,
          "question": question.question,
          "answer": question.answer,
          "type": question.type.toUpperCase(),
          "choices": question.choices
        };
        addQuestionPromises.push(addQuestion(questionCreateDTO));
      });

      await Promise.all(addQuestionPromises)
          .then(responses => {
            createdQuiz = responses[responses.length - 1];
          }).catch(error => {
            console.warn("Error adding questions:", error);
          });

      const addKeywordPromises = [];
      this.templateQuiz.keywords.forEach(keyword => {
        const keywordDTO = {
          "keywordId": keyword.categoryId,
          "keywordName": keyword
        };
        addKeywordPromises.push(addKeyword(keywordDTO));
      });

      await Promise.all(addKeywordPromises)
          .then(responses => {
            console.log("Keywords added:", responses);
          }).catch(error => {
            console.warn("Error adding keywords:", error);
          });

      const addCategoryPromises = [];
      this.templateQuiz.categories.forEach(category => {
        const categoryDTO = {
          "categoryId": category.categoryId,
          "categoryName": category.newName
        };
        addCategoryPromises.push(addCategory(categoryDTO));
      });

      await Promise.all(addCategoryPromises)
          .then(responses => {
            console.log("Categories added:", responses);
          }).catch(error => {
            console.warn("Error adding categories:", error);
          });

      await updateQuiz(quizUpdateDTO)
          .then(response => {
            console.log(response);
            createdQuiz = response;
          }).catch(error => {
            console.warn("Error updating quiz:", error);
          });
    },
  },
  persist: {
    storage: sessionStorage
  }
})
