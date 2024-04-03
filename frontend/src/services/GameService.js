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

    // Create a new game
    createGame(jwt, quizId, callback) {
        this.socket.emit('createGame', { jwt, quizId }, callback);
    }

    // Join an existing game
    joinGame(code, jwt, username, callback) {
        this.socket.emit('joinGame', { code, jwt, username }, callback);
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
    answerQuestion(gameCode, jwt, answer, callback) {
        this.socket.emit('answerQuestion', { gameCode, jwt, answer }, callback);
    }

    onbeginAnswering(callback) {
        this.socket.on('beginAnswering', callback);
    }

    onGameEnded(callback) {
        this.socket.on('gameEnded', callback);
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
        this.socket.on('everyOneAnswered', callback);
    }

    onGetScoreBoard(callback) {
        this.socket.on('getScoreBoard', callback);
    }

    onPlayerJoined(callback) {
        this.socket.on('playerJoined', callback);
    }





    onAnswerRevealed(callback) {
        this.socket.on('answerRevealed', callback);
    }

}

// Instantiate and export the service
const gameService = new GameService();
export default gameService;
