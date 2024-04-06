import io from 'socket.io-client';

class GameService {

    serverUrl = 'http://localhost:3000';
    constructor() {
        this.socket = null;
    }

    // Connects to the WebSocket server
    connect() {
        this.socket = io(this.serverUrl);

        this.socket.on('connect', () => {
            console.log('Connected to the game server');
        });

        // Handle other global events, like disconnection or server-wide messages
        this.socket.on('disconnect', () => {
            console.log('Disconnected from the game server');
        });

        // Add handlers for other events sent by the server as needed
    }

    disconnect() {
        this.socket.disconnect();
    }

    // Create a new game
    createGame(jwt, quizId, callback) {
        this.socket.emit('createGame', { jwt, quizId }, callback);
    }

    // Join an existing game
    joinGame(joinGameDTO, callback) {
        this.socket.emit('joinGame', joinGameDTO, callback);
    }

    onGameJoined(callback) {
        this.socket.on('gameJoined', callback);
    }

    onDefaultImages(callback) {
        this.socket.on('defaultImages', callback);
    }

    checkGameExists(gameCode, callback) {
        this.socket.emit('checkGameExists', gameCode, callback);
    }

    onbeginAnswering(callback) {
        this.socket.on('beginAnswering', callback);
    }

    answerQuestion(ansertdto, callback) {
        this.socket.emit('answerQuestion', ansertdto, callback);
    }

    // Start the game (host only)
    startGame(gameCode, jwt, callback) {
        this.socket.emit('startGame', { gameCode, jwt }, callback);
    }

    // Move to the next question (host only)
    nextQuestion(gameCode, jwt, callback) {
        this.socket.emit('nextQuestion', { gameCode, jwt }, callback);
    }

    beginAnswering(gameCode, jwt, callback) {
        this.socket.emit('beginAnswering', { gameCode, jwt }, callback);
    }

    revealAnswer(gameCode, jwt, callback) {
        this.socket.emit('revealAnswer', { gameCode, jwt }, callback);
    }

    getScoreBoard(gameCode, jwt, callback) {
        this.socket.emit('getScoreBoard', { gameCode, jwt }, callback);
    }

    // Submit an answer to the current question
    onWaitForQuestion(callback) {
        this.socket.on('waitForQuestion', callback);

    }



    onGameEnded(callback) {
        this.socket.on('gameEnded', callback);
    }

    onQuizId(callback) {
        this.socket.on('quizId', callback);
    }

    onGameDoesNotExist(callback) {
        this.socket.on('gameDoesNotExist', callback);
    }

    onGameExists(callback) {
       this.socket.on('gameExists', callback);
    }


    onYourScore(callback) {
        this.socket.on('yourScore', callback);
    }
    // Listen for updates about the game, like scores, current question, etc.


    onGameCreated(callback) {
        this.socket.on('gameCreated', callback);
    }

    onGetScoreBoard(callback) {
        this.socket.on('getScoreBoard', callback);
    }

    onNotHost(callback) {
        this.socket.on('notHost', callback);
    }

    onGetQuestion(callback) {
        this.socket.on('getQuestion', callback);
    }

    onEveryOneAnswered(callback) {
        this.socket.on('everyoneAnswered', callback);
    }


    onPlayerJoined(callback) {
        this.socket.on('playerJoined', callback);
    }

    onPlayerLeft(callback) {
        this.socket.on('playerLeft', callback);
    }


    onAnswerRevealed(callback) {
        this.socket.on('answerRevealed', callback);
    }

}

// Instantiate and export the service
const gameService = new GameService();
export default gameService;
