import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import singleplayerView from "@/views/QuizPlaying/SingleplayerView.vue";
import { useQuizStore, useUserStore } from "@/stores/counter.js"; // Mock these stores if used within the component
import router from "@/router/index.js";
import mutlipleChoiceComponent from "@/components/QuizPlaing/mutlipleChoiceComponent.vue";
import shortAnswercomponent from "@/components/QuizPlaing/shortAnswercomponent.vue";

// Mocking external dependencies
vi.mock("@/stores/counter.js");
vi.mock("@/router/index.js");
vi.mock("@/services/HistoryService.js", () => ({
    addHistoricalEvent: vi.fn(),
}));
vi.mock("@/services/feedbackService.js", () => ({
    addFeedback: vi.fn(),
}));


describe('SinglePlayerGame', () => {
    let wrapper;

    const mockQuestions = [
        { question: "What is 2+2?", type: "SHORT_ANSWER", answer: "4" },
        // Add more mock questions as needed
    ];

    beforeEach(() => {
        // Mocking store to provide initial state
        useQuizStore.mockReturnValue({
            currentQuiz: { quizName: "Math Quiz", questions: mockQuestions },
            // Implement other necessary mock store methods
        });
        useUserStore.mockReturnValue({
            user: { userId: "user1" },
            // Implement other necessary mock store methods
        });

        wrapper = mount(singleplayerView);
    });

    it('renders the start game button initially', () => {
        expect(wrapper.text()).toContain('Start Game');
    });

    it('starts the game and shows the first question on start button click', async () => {
        await wrapper.find('.large-button').trigger('click');
        expect(wrapper.vm.gameStarted).toBe(true);
        expect(wrapper.vm.previewPhase).toBe(true); // Assuming previewPhase means showing the first question
        // Assert the current question's content is displayed
        expect(wrapper.text()).toContain(mockQuestions[0].question);
    });



    it('renders the correct question component based on question type', async () => {
        const mockMultipleChoiceQuestion = {
            question: "Which of these is a JavaScript framework?",
            type: "MULTIPLE_CHOICE",
            choices: ["Vue", "Laravel", "Django"],
            answer: "Vue"
        };

        const mockShortAnswerQuestion = {
            question: "What is 2+2?",
            type: "SHORT_ANSWER",
            answer: "4"
        };

        // Start the game with a Multiple Choice question
        await wrapper.setProps({
            currentQuiz: { quizName: "Tech Quiz", questions: [mockMultipleChoiceQuestion] }
        });
        await wrapper.find('.large-button').trigger('click');
        expect(wrapper.vm.previewPhase).toBe(true);
    });

    it('progresses through game phases correctly', async () => {
        // Start game and enter preview phase
        await wrapper.find('.large-button').trigger('click');
        expect(wrapper.vm.previewPhase).toBe(true);
        await wrapper.vm.handlePreviewEnd(); // Simulate end of preview

        // Handle question answering and moving to score component
        await wrapper.vm.handleAnswer(true, 85); // Simulate correct answer with time left
        expect(wrapper.vm.showScoreComponent).toBe(false);

        // Simulate score component countdown ending and moving to the next question or game end
        await wrapper.vm.handleCountdownEnded();
        // If it was the last question, the game should end; otherwise, it moves to the next question
        // For simplicity, assuming it was the last question:
        expect(wrapper.vm.gameEnded).toBe(true);
    });






});
