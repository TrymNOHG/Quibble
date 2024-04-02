import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:8080/api/v1/private/quiz"

export const createQuiz = async (quizName) => {
    try {
        const response = await axios.post(`${BASE_URL}/create/${quizName}`,
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
        const response = await axios.patch(`${BASE_URL}/update`,
            quizUpdateDTO, {
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

export const addCollaborator = async (quizAuthorDTO) => {
    try {
        const response = await axios.post(`${BASE_URL}/create/collaborator`,
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

export const removeCollaborator = async (username) => {
    try {
        const response = await axios.delete(`${BASE_URL}/delete/${username}`,
            {
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

export const deleteQuizById = async (QuizId) => {
    try {
        const response = await axios.delete(`${BASE_URL}/delete/${QuizId}`, {
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



export const addQuestion = async (questionCreateDTO) => {
    try {
        const response = await axios.post(`${BASE_URL}/create/question`,
            questionCreateDTO, {
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

