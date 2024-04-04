/**
 * This method generates a quizCreateDTO from a csv file
 * @param csvFile
 * @returns {{difficulty: null, quizName: null, questions: [], quizDescription: null, categories: []}}
 */
const createQuizCreateDTOFromCSV = (csvFile) => {
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
        throw Error("Invalid format")
    }
    [quizCreateDTO.quizName, quizCreateDTO.quizDescription, quizCreateDTO.difficulty] = firstValues

    let categories = lines.shift().split(",")
    if(categories[0] !== "") {
        quizCreateDTO.categories = categories
    }

    for(let line in lines) {
        let question = line.split(",")
        if(question.length === 3) {
            let [question, answer, type] = lines.shift().split(",")
            quizCreateDTO.questions.push({question, answer, type})
        } else if(question.length === 4) {
            let [question, answer, type, choicesLength] = lines.shift().split(",")
            let choices = []
            for(let i = 0; i < choicesLength; i++) {
                let [alternative, isCorrect] = lines.shift().split(",")
                choices.push({alternative, isCorrect})
            }
            quizCreateDTO.questions.push({question, answer, type, choices})
        } else {
            throw Error("Invalid format.")
        }
    }

    return quizCreateDTO
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
const downloadQuizCSV = (quizCreateDTO, fileName) => {
    if(quizCreateDTO === null){
        throw Error("Invalid quiz DTO")
    }
    const lines = []
    lines[0] = [quizCreateDTO.quizName, quizCreateDTO.quizDescription, quizCreateDTO.difficulty].join(",")
    lines[1] = quizCreateDTO.categories.join(",")
    for(let question in quizCreateDTO.questions) {
        lines.push([question.question, question.answer, question.type, question.choices.length].join(","))
        if (question.type.toUpperCase() === 'MULTIPLE_CHOICE') {
            for (let choice in question.choices) {
                lines.push([choice.alternative, choice.isCorrect].join(","))
            }
        }
    }

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

    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}