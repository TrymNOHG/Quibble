const BASE_URL = "http://localhost:8080/api/v1"

export const getPictureFromID =  (id) => {
    return `${BASE_URL}/public/image/${id}`;
};

export const createQuizImage = (imgDTO) => {
    console.log(imgDTO);
    return `${BASE_URL}/public/image/${id}`;
}




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
