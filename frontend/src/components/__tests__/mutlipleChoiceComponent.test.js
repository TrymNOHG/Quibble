import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import mutlipleChoiceComponent from "@/components/QuizPlaing/mutlipleChoiceComponent.vue";
// Mocking external dependencies


describe('MyComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(mutlipleChoiceComponent, {
            props: {
                question: {
                    question: 'What is 2+2?',
                    options: [
                        { alternative: '3', isCorrect: false },
                        { alternative: '4', isCorrect: true }
                    ]
                },
                isSinglePlayer: true,
                showAnswersProp: false,
                isMultiplayerClient: false,
            },
        });
    });

    it('renders question and answers correctly', () => {
        expect(wrapper.html()).toContain('What is 2+2?');
        expect(wrapper.findAll('.answer-card').length).toBe(2);
    });

    it('selects an answer and emits event', async () => {
        await wrapper.find('.answer-card:nth-child(2)').trigger('click');
        expect(wrapper.emitted()).toHaveProperty('answerSelected');
        // Additional checks for the emitted event payload can be added here
    });


    // Add more tests as needed
});
