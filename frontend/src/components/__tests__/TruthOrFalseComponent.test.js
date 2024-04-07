import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import truthOrFalseComponent from "@/components/QuizPlaing/TruthOrFalseComponent.vue";
import correctSoundFile from "@/assets/sound/correct.mp3";
import wrongSoundFile from "@/assets/sound/wrong.mp3";
import timerSoundFile from "@/assets/sound/timer.mp3";




describe('truthOrFalseComponent', () => {
    let wrapper;

    beforeEach(() => {
        vi.useFakeTimers();
        wrapper = mount(truthOrFalseComponent, {

            props: {
                question: {
                    question: 'Is this a test?',
                    answer: 'true',
                },
                isSinglePlayer: true,
                showAnswersProp: false,
                duration: 90,
                isMultiplayerClient: false,
            },
        });
    });


    it('renders correctly', () => {
        expect(wrapper.vm.question.question).toBe('Is this a test?');
    });

    it('correctly computes isCorrectTrue', async () => {
        await wrapper.setProps({ question: { question: 'Is this true?', answer: 'true' } });
        expect(wrapper.vm.isCorrectTrue).toBe(true);
        expect(wrapper.vm.isCorrectFalse).toBe(false);
    });



    it('updates selectedAnswer and showAnswers on selecting true', async () => {
        await wrapper.find('.true-card').trigger('click');
        expect(wrapper.vm.selectedAnswer).toBe(true);
        expect(wrapper.vm.showAnswers).toBe(true);
    });

    it('updates selectedAnswer and showAnswers on selecting false', async () => {
        await wrapper.find('.false-card').trigger('click');
        expect(wrapper.vm.selectedAnswer).toBe(false);
        expect(wrapper.vm.showAnswers).toBe(true);
    });


    it('reacts to showAnswersProp changes', async () => {
        expect(wrapper.vm.showAnswers).toBe(false); // Initial state
        await wrapper.setProps({ showAnswersProp: true });
        expect(wrapper.vm.showAnswers).toBe(true); // Updated state
    });


    it('does not flip cards in multiplayer client mode', async () => {
        await wrapper.setProps({ isMultiplayerClient: true });
        await wrapper.find('.true-card').trigger('click');
        expect(wrapper.find('.true-card').classes()).not.toContain('flip');
    });

    it('does not show checkmarks or crosses in multiplayer client mode', async () => {
        await wrapper.setProps({ isMultiplayerClient: true, showAnswersProp: true });
        expect(wrapper.find('.card-back span').exists()).toBe(false);
    });




    it('updates progress bar width over time', async () => {
        expect(wrapper.vm.progressBarWidth).toBe(100); // Initial state
        vi.advanceTimersByTime(30000); // Simulate 30 seconds passing
        await wrapper.vm.$nextTick(); // Wait for Vue to update the DOM
        expect(wrapper.vm.progressBarWidth).toBeLessThan(100); // Check if the progress bar width decreased
    });

    it('clears timer on component unmount', async () => {
        vi.spyOn(global, 'clearInterval');
        wrapper.unmount();
        expect(clearInterval).toHaveBeenCalled();
    });

});

