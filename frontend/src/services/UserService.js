import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:8080/api/v1"

// let userLoginDTO = {
//     'emailOrUserName' : '',
//     'password' : ''
// }
export const loginUser = async (userLoginDTO) => {
    const base64Credentials = btoa(userLoginDTO.emailOrUserName + ':' + userLoginDTO.password);
    return axios.post(`${BASE_URL}/public/auth/login`, {},{
        headers: {
            'Authorization': 'Basic ' + base64Credentials
        }
    });
}

// let userRegisterDTO = {
//     'username' : '',
//     'password' : '',
//     'email'    : ''
// }
export const registerUser = async (userRegisterDTO) => {
    return axios.post(`${BASE_URL}/public/auth/signup`, userRegisterDTO);
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
        data : { quizId },
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const getSearchedQuizzes = async (searchword) => {
    return axios.get(`${BASE_URL}/quiz/searchQuiz`, {
        data: { searchword },
    })
}

export const getMoreQuizzes = async (diff) => {
    return axios.get(`${BASE_URL}/quiz/moreQuizzes`, {
        data: { diff }
    })
}

export const getQuizzesByDifficulty = async (diff) => {
    return axios.get(`${BASE_URL}/quiz/QuizzDiff`, {
        data: { diff }
    })
}

// Public Endpoints



// Private Endpoints

// let userUpdateDTO = {
//     'userId' : '',
//     'username' : '',
//     'profilePicture' : null
// }
export const updateUser = async (userUpdateDTO) => {
    return axios.patch(`${BASE_URL}/private/users/update`, userUpdateDTO, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    });
}

export const deleteUser = async () => {
    return axios.delete(`${BASE_URL}/private/users/update`, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    });
}