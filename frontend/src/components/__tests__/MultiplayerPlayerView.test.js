import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import multiplayerHostView from "@/views/QuizPlaying/MultiplayerHostView.vue"; // Adjust the import path as needed
import gameService from '@/services/GameService.js';
import { useQuizStore } from '@/stores/counter.js';
import scoreComponent from "@/components/QuizPlaing/scoreComponent.vue";
import {setMockToken} from "@/features/SessionToken.js"; // Assuming you're using Pinia or a similar store

// Mock external dependencies
vi.mock("@/services/GameService.js", () => ({
    __esModule: true,
    default: {
        connect: vi.fn(),
        disconnect: vi.fn(),
        createGame: vi.fn(),
        startGame: vi.fn(),
        revealAnswer: vi.fn(),
        beginAnswering: vi.fn(),
        getScoreBoard: vi.fn(),
        nextQuestion: vi.fn(),
        onGameCreated: vi.fn((callback) => callback("GAMECODE123")),
        onPlayerJoined: vi.fn((callback) => callback({ id: "player1", username: "Player1" })),
        onPlayerLeft: vi.fn((callback) => callback({ id: "player1", username: "Player1" })),
        onGetQuestion: vi.fn((callback) => callback({ question: "What is 2+2?", answer: "4" })),
        onAnswerRevealed: vi.fn(),
        onEveryOneAnswered: vi.fn(),
        onGetScoreBoard: vi.fn(),
        onGameEnded: vi.fn(),
        // Add other methods as necessary
    },
}));


vi.mock('@/stores/counter.js', () => ({
    useQuizStore: vi.fn(() => ({
        currentQuiz: { quizId: '123', quizName: 'Sample Quiz' },
        // Mock other store properties and methods as needed
    })),
}));

// Mock any additional services or stores as necessary

function ShortAnswer() {

}

describe('GameHost', () => {
    let wrapper;
    setMockToken('mockToken');

    beforeEach(() => {
        wrapper = mount(multiplayerHostView);
    });

    it('renders the start game button initially', () => {
        expect(wrapper.text()).toContain('QuizSpillkode: GAMECODE123Start Spill');
    });



});


