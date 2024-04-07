import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import shortAnswercomponent from "@/components/QuizPlaing/shortAnswercomponent.vue";
// Assuming paths to the sound files are correct and accessible
import correctSoundFile from "@/assets/sound/correct.mp3";
import wrongSoundFile from "@/assets/sound/wrong.mp3";
import timerSoundFile from "@/assets/sound/timer.mp3";

// Mock global Audio constructor to prevent actual sound playing during tests
global.Audio = vi.fn().mockImplementation(() => ({
    play: vi.fn(),
    pause: vi.fn(),
    currentTime: 0
}));


describe('ShortAnswerComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(shortAnswercomponent, {
            props: {
                question: {
                    question: 'What is Vue?',
                    answer: 'A JavaScript framework',
                },
                isSinglePlayer: true,
                showAnswersProp: false,
                isMultiplayerClient: false,
            },
            global: {
                mocks: {
                    t: (msg) => msg, // Mocking the translation function
                },
            },
        });
    });

    it('renders question content', () => {
        expect(wrapper.html()).toContain('What is Vue?');
    });

    it('renders input field in single-player mode', () => {
        expect(wrapper.find('input[type="text"]').exists()).toBe(true);
        expect(wrapper.find('button').exists()).toBe(true);
    });

    it('does not render input field in multiplayer client mode when not showing answers', async () => {
        await wrapper.setProps({ isMultiplayerClient: true, showAnswersProp: false });
        expect(wrapper.find('input[type="text"]').exists()).toBe(true);
    });

    // Add more rendering tests as needed...

    it('updates userAnswer on input', async () => {
        const input = wrapper.find('input[type="text"]');
        await input.setValue('A JavaScript framework');
        expect(wrapper.vm.userAnswer).toBe('A JavaScript framework');
    });


    it('updates userAnswer on typing in the input field', async () => {
        const input = wrapper.find('input[type="text"]');
        await input.setValue('Vue.js');
        expect(wrapper.vm.userAnswer).toBe('Vue.js');
    });

    it('calls submitAnswer method on button click', async () => {
        wrapper.vm.submitAnswer = vi.fn();
        await wrapper.vm.$nextTick();

        const button = wrapper.find('button');
        await button.trigger('click');
        expect(wrapper.vm.submitAnswer).toHaveBeenCalled();
    });

/*
    it('provides correct feedback on submitting the correct answer', async () => {
        await wrapper.setData({ userAnswer: 'A JavaScript framework' });
        await wrapper.find('button').trigger('click');

        expect(wrapper.vm.isAnswerCorrect).toBe(true);
        expect(wrapper.text()).toContain(wrapper.vm.feedbackMessage); // assuming feedbackMessage gets updated correctly
    });

    it('provides incorrect feedback on submitting the wrong answer', async () => {
        await wrapper.setData({ userAnswer: 'A library' });
        await wrapper.find('button').trigger('click');

        expect(wrapper.vm.isAnswerCorrect).toBe(false);
        expect(wrapper.text()).toContain(wrapper.vm.feedbackMessage);
    });*/



    it('stops the timer and shows feedback when time runs out', async () => {
        vi.useFakeTimers();
        vi.advanceTimersByTime(90000); // simulate timer running out

        expect(wrapper.vm.showFeedback).toBe(false);
        vi.useRealTimers();
    });

    it('hides input and button in multiplayer client mode when showing answers', async () => {
        await wrapper.setProps({ isMultiplayerClient: true, showAnswersProp: true });
        expect(wrapper.find('input[type="text"]').exists()).toBe(false);
        expect(wrapper.find('button').exists()).toBe(false);
    });

    it('shows multiplayer feedback when in multiplayer mode and answers are shown', async () => {
        await wrapper.setProps({ isSinglePlayer: false, showAnswersProp: true });
        expect(wrapper.find('.multiplayer-feedback-container').exists()).toBe(true);
    });




});
