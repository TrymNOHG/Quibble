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
    const formData = new FormData();
    for (const key in userRegisterDTO) {
        formData.append(key, userRegisterDTO[key]);
    }
    return axios.post(`${BASE_URL}/public/auth/signup`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}


export const checkSuperUser = async (quizId) => {
    return axios.get(`${BASE_URL}/superuser`, {
        data : { quizId },
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

export const fetchUserByUsername = async (username) => {
    try {
        const response = await axios.get(`${BASE_URL}/public/users/get?username=${username}&number=10`,{
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        console.log(response.data)
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

// Public Endpoints



// Private Endpoints

export const getUser = async () => {
    return axios.get(`${BASE_URL}/private/users/get`, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    })
}

// let userUpdateDTO = {
//     'userId' : '',
//     'username' : '',
//     'profilePicture' : null,
//     'showActivity' : null,
//      'showFeedback' : null
// }
export const updateUser = async (userUpdateDTO) => {
    return axios.patch(`${BASE_URL}/private/users/update`, userUpdateDTO, {
        headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${await sessionToken()}`,
        },
    });
}


//TODO: use userUpdateDTO
export const updateUserShowActivity = async (newShowActivity) => {
    console.log(newShowActivity)
    return axios.patch(`${BASE_URL}/private/users/update/showActivity`, newShowActivity, {
        headers: {
            Authorization: `Bearer ${await sessionToken()}`,
        },
    });
}

export const updateUserShowFeedback = async (newShowFeedback) => {
    return axios.patch(`${BASE_URL}/private/users/update/showFeedback`, newShowFeedback, {
        headers: {
            'Content-Type': 'multipart/form-data',
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