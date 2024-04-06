import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL_PRIV = "http://localhost:8080/api/v1/private/quiz"
const BASE_URL_PUB = "http://localhost:8080/api/v1/public/quiz"


export const fetchQuizzes = async (page, size) => {
    try {
        const response = await axios.get(`${BASE_URL_PUB}/get?page=${page}&size=${size}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data.content;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const createQuiz = async (quizName) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/${quizName}`,
            {},
            {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const updateQuiz = async (quizUpdateDTO) => {
    try {
        const response = await axios.patch(`${BASE_URL_PRIV}/update`,
            quizUpdateDTO, {
            headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const deleteQuizById = async (QuizId) => {
    try {
        const response = await axios.delete(`${BASE_URL_PRIV}/delete/${QuizId}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const addQuestion = async (questionCreateDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/question`,
            questionCreateDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const deleteQuestionById = async (questionId) => {
    try {
        const response = await axios.delete(`${BASE_URL_PRIV}/delete/question/${questionId}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const patchQuestion = async (questionEditDTO) => {
    try {
        const response = await axios.patch(`${BASE_URL_PRIV}/edit/question`,
            questionEditDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const addCollaborator = async (quizAuthorDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/collaborator`,
            quizAuthorDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        console.log(response);
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const removeCollaborator = async (authorId) => {
    try {
        const response = await axios.delete(`${BASE_URL_PRIV}/delete/collaborator/${authorId}`,
            {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const addCategory = async (categoryDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/category`,
            categoryDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        console.log("cat  ", response.data)
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const addCategories = async (categoriesDTO) => {
    if(categoriesDTO === null) {
        throw Error("Category DTO is null")
    }
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/categories`,
            categoriesDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const addKeyword = async (keywordDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/question`,
            keywordDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        console.log("key  ", response.data)
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};
