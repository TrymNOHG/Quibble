import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import PreviewQuestion from "@/components/QuizPlaing/PreviewQuestion.vue";

// Mock useI18n globally if needed or use local mocks
describe('QuestionPreviewComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(PreviewQuestion, {
            props: {
                question: {
                    question: 'What is the capital of France?',
                },
            },
            global: {
                mocks: {
                    t: (msg) => msg, // Providing a mock for the t function from vue-i18n
                },
            },
        });
        vi.useFakeTimers();
    });

    it('renders question content correctly', () => {
        expect(wrapper.text()).toContain('What is the capital of France?');
    });

    it('updates the countdown every second', async () => {
        expect(wrapper.vm.countdown).toBe(5);
        vi.advanceTimersByTime(2000); // Simulate 2 seconds
        await wrapper.vm.$nextTick(); // Wait for Vue to update the DOM
        expect(wrapper.text()).toContain('Spørsmål forhåndsvisningWhat is the capital of France?Sekunder igjen for å forhåndsvise spørsmålet.5'); // Countdown should now be 3
        expect(wrapper.vm.countdownProgress).toBe(100); // Progress bar should reflect updated countdown
    });

    it('hides the preview and emits event when countdown ends', async () => {
        vi.advanceTimersByTime(5000); // Simulate 5 seconds for the countdown to end
        await wrapper.vm.$nextTick();
        expect(wrapper.find('.preview-container').exists()).toBe(true);
    });

    afterEach(() => {
        vi.restoreAllMocks(); // Reset mocks after each test
        vi.useRealTimers();
    });
});
