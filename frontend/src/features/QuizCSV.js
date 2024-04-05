/**
 * This method generates a quizCreateDTO from a csv file
 * @returns {{difficulty: null, quizName: null, questions: [], quizDescription: null, categories: []}}
 * @param event
 */
export const createQuizCreateDTOFromCSV = async (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();

    return new Promise((resolve, reject) => {
        reader.onload = (e) => {
            const csvFile = e.target.result;
            const lines = csvFile.split("\n");

            let quizCreateDTO = {
                quizName: null,
                quizDescription: null,
                difficulty: null,
                categories: [],
                questions: []
            }

            let firstValues = lines.shift().split(",")
            if (firstValues.length !== 3) {
                throw Error("Invalid format of first row")
            }
            [quizCreateDTO.quizName, quizCreateDTO.quizDescription, quizCreateDTO.difficulty] = firstValues

            let categories = lines.shift().split(",")
            if (categories[0] !== "") {
                quizCreateDTO.categories = categories
            }

            for (let i = 0; i < lines.length; i++) {
                let question = lines[i].split(",")
                if (question.length === 3) {
                    let [question, answer, type] = lines.shift().split(",")
                    quizCreateDTO.questions.push({question, answer, type})
                } else if (question.length === 4) {
                    let [question, answer, type, choicesLength] = lines.shift().split(",")
                    let choices = []
                    for (let j = 0; j < choicesLength; j++) {
                        let [alternative, isCorrect] = lines.shift().split(",")
                        choices.push({alternative, isCorrect})
                    }
                    quizCreateDTO.questions.push({question, answer, type, choices})
                } else {
                    throw Error("Invalid format.")
                }
                i -= 1;
            }


            // console.log(quizCreateDTO)
            resolve(quizCreateDTO);
        };

        reader.onerror = function () {
            reject(new Error('Failed to read the file.'));
        };
        reader.readAsText(file);
    });

}

/**
 * This method provides functionality for downloading a quiz as a csv.
 * Structure:
 * <p>
 *      meta-info:                           quizName, quizDescription, difficulty
 *      categories:                          Sports, Science   (example)
 *      questions:                           question, answer, questionType
 *      if questionType multiple choice:     alternative, isCorrect
 *
 * </p>
 * @param quizCreateDTO
 * @param fileName
 */
export const downloadQuizCSV = (quizCreateDTO, fileName) => {
    if(quizCreateDTO === null){
        throw Error("Invalid quiz DTO")
    }
    console.log(quizCreateDTO.quizName)
    const lines = []
    lines[0] = [quizCreateDTO.quizName, quizCreateDTO.quizDescription, quizCreateDTO.difficulty].join(",")
    if(quizCreateDTO.categories && quizCreateDTO.categories.length !== 0) {
        lines[1] = quizCreateDTO.categories.join(",")
    } else {
        lines[1] = ""
    }
    for(let question of quizCreateDTO.questions) {
        if(question.choices && question.choices.length !== 0){
            lines.push([question.question, question.answer, question.type, question.choices.length].join(","))
            if (question.type.toUpperCase() === 'MULTIPLE_CHOICE') {
                for (let choice of question.choices) {
                    lines.push([choice.alternative, choice.isCorrect].join(","))
                }
            }
        } else{
            lines.push([question.question, question.answer, question.type].join(","))
        }

    }
    console.log(lines)
    console.log(quizCreateDTO)
    const csvData = lines.join("\n")
    downloadCSV(csvData, fileName)
}

/**
 * This method provides general downloading of csv files.
 * @param data          The data to be downloaded.
 * @param filename      The name of the file.
 */
const downloadCSV = (data, filename) => {
    const blob = new Blob(
        [data],
        { type: 'text/csv;charset=utf-8' }
    );

    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();

    URL.revokeObjectURL(url);
}