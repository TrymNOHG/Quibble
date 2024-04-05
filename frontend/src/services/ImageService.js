const BASE_URL = "http://localhost:8080/api/v1"

export const getPictureFromID =  (id) => {
    return `${BASE_URL}/public/image/${id}`;
};

