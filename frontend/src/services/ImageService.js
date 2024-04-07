import sessionToken from "@/features/SessionToken.js";
import axios from "axios";

const BASE_URL = "http://localhost:8080/api/v1"

export const getPictureFromID =  (id) => {
    return `${BASE_URL}/public/image/${id}`;
};

export const saveFile = async (imgDTO) => {
    try {
        const imageFile = new FormData();
        imageFile.append('quizId', imgDTO.quizId);
        imageFile.append('image', imgDTO.quizImage);

        const response = await axios.post(`${BASE_URL}/private/image/quiz/save`,
            imageFile, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${await sessionToken()}`,
            }
        });
        return response.data;
    } catch (error) {
        throw error.response ? error.response.data : error.message;
    }
};



// axios.get('http://localhost:8080/images/user123/avatar.jpg', )
//     .then(response => {
//         // Convert the received binary data to a base64 string
//         const imageBase64 =
//
//         // Set the src attribute of the <img> tag to display the image
//         document.getElementById('image').src = 'data:image/jpeg;base64,' + imageBase64;
//     })
//     .catch(error => {
//         console.error('Error fetching image:', error);
//     });
