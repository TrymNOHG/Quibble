import axios from "axios";
import sessionToken from "@/features/SessionToken.js";

const BASE_URL_PRIV_FEEDBACK = "http://localhost:8080/api/v1/private/feedback";

export const addFeedback = async (newFeedbackDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV_FEEDBACK}/create`, newFeedbackDTO, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const updateFeedback = async (updatedFeedbackDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV_FEEDBACK}/update`, updatedFeedbackDTO, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const deleteFeedback = async (quizId) => {
    try {
        const response = await axios.delete(`${BASE_URL_PRIV_FEEDBACK}/delete/${quizId}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const getFeedbacks = async (quizId) => {
    try {
        const response = await axios.get(`${BASE_URL_PRIV_FEEDBACK}/get/${quizId}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};
