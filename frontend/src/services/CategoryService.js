import axios from "axios";

const BASE_URL_PUB = "http://localhost:8080/api/v1/public/category"

export const getAllCategories = async () => {
    try {
        const response = await axios.get(`${BASE_URL_PUB}/getAll`,
             {
                headers: {
                }
            });
        console.log(response.data)
        return response.data.categories;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};