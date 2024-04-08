import axios from "axios";
import sessionToken from "@/features/SessionToken.js";

const BASE_URL_PRIV = "http://localhost:8080/api/v1/private/history";

export const addHistoricalEvent = async (historicalEventDTO) => {
    try {
        const response = await axios.post(`${BASE_URL_PRIV}/create/event`,
            historicalEventDTO, {
                headers: {
                    Authorization: `Bearer ${await sessionToken()}`,
                }
            });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};

export const getAllHistoricalEvents = async (userId) => {
    try {
        const response = await axios.get(`${BASE_URL_PRIV}/${userId}`, {
            headers: {
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};