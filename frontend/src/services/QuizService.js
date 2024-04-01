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
