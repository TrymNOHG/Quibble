import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:5173/api/user"

export const loginUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/login`, userDTO);
}

export const registerUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/register`, userDTO);
}

export const getUser = async () => {
    return axios.get(`${BASE_URL}/user/get/info`, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const checkSuperUser = async (quizId) => {
    return axios.get(`${BASE_URL}/superuser`, {
        params : { quizId },
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}


// set in another service
export const getRecommendedQuizList = async () => {
    return axios.get(`${BASE_URL}/quiz/getRecommended`, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const getSearchedQuizzes = async (searchword) => {
    return axios.get(`${BASE_URL}/quiz/searchQuiz`, {
        params: { searchword },
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}
