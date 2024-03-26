import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:8080/api/v1/"

// Public Endpoints

// let userLoginDTO = {
//     'emailOrUserName' : '',
//     'password' : ''
// }
export const loginUser = async (userLoginDTO) => {
    return axios.post(`${BASE_URL}/public/users/login`, userLoginDTO);
}

// let userRegisterDTO = {
//     'username' : '',
//     'password' : '',
//     'email'    : ''
// }
export const registerUser = async (userRegisterDTO) => {
    return axios.post(`${BASE_URL}/public/users/register`, userRegisterDTO);
}

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