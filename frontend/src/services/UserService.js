import axios from "axios"
import sessionToken from "@/features/SessionToken.js";

const BASE_URL = "http://localhost:5173/api/user"

export const loginUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/login`, userDTO);
}

export const registerUser = async (userDTO) => {
    return axios.post(`${BASE_URL}/register`, userDTO);
}
