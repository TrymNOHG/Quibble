// Import necessary utilities from Vitest and Vue Test Utils
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import QuizCard from '@/components/BasicComponents/one_quiz_rectangle.vue';
import PublicProfileComponent from "@/components/Profile/PublicProfileComponent.vue";

// Mock global window properties if necessary
global.innerWidth = 1024; // Set initial window width for testing

// Define the describe block for the ProfileAndQuizView component tests
describe('PublicProfileComponent', () => {
    let wrapper;

    beforeEach(() => {
        // Mock user data
        const mockUser = {
            profilePicture: 'https://via.placeholder.com/100',
            username: 'JohnDoe',
        };

        const defaultPicture = 'https://via.placeholder.com/100';
        // Provide the mock user as a prop to the component
        wrapper = mount(PublicProfileComponent, {
            props: {
                user: mockUser,
                quizzes: [{ id: 1, name: 'Almost Best Quiz1', rating: 4, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 2, name: 'Best Quiz2', rating: 5, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 3, name: 'Almost Best Quiz3', rating: 4, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 4, name: 'Best Quiz4', rating: 5, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 5, name: 'Almost Best Quiz5', rating: 4, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 6, name: 'Best Quiz6', rating: 5, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 7, name: 'Almost Best Quiz7', rating: 4, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 8, name: 'Best Quiz8', rating: 5, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 9, name: 'Almost Best Quiz9', rating: 4, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                    { id: 10, name: 'Best Quiz10', rating: 5, image: defaultPicture, question_list: ["Question 1", "Question 2", "Question 3"] },
                ] // Assuming quizzes prop is also required but can be an empty array for now
            },
            global: {
                stubs: { QuizCard: true }, // Stub the QuizCard component
                mocks: { $t: msg => msg }, // Localization mock
            },
        });
    });


    it('renders user info and profile picture correctly', () => {
        const userInfo = wrapper.find('.user-info');
        const profilePicture = userInfo.find('.profile-picture');
        const username = userInfo.find('.username');

        expect(profilePicture.exists()).toBe(true);
        expect(profilePicture.attributes('src')).toContain('https://via.placeholder.com/100');
        expect(username.text()).toContain('JohnDoe');
    });

    it('displays the correct number of visible quizzes based on window width', async () => {
        // Assuming the initial window width set to 1024 should show 3 quizzes
        expect(wrapper.findAll('.carousel-slide').length).toBe(3);
    });

    it('scrolls to the next set of quizzes when next button is clicked', async () => {
        await wrapper.find('.next-button').trigger('click');
        expect(wrapper.vm.currentIndex).toBe(1); // Assuming currentIndex changes as expected
    });

    it('scrolls to the previous set of quizzes when prev button is clicked', async () => {
        await wrapper.find('.prev-button').trigger('click');
        expect(wrapper.vm.currentIndex).toBe(-1);
    });

    it('applies gradient correctly when the number of visible quizzes is more than 2', () => {
        // Resize window to make sure more than 2 quizzes are visible if needed
        global.innerWidth = 1440;
        window.dispatchEvent(new Event('resize'));

        const firstQuiz = wrapper.findAll('.carousel-slide').at(0);
        const lastQuiz = wrapper.findAll('.carousel-slide').at(wrapper.findAll('.carousel-slide').length - 1);

        expect(firstQuiz.classes()).toContain('apply-gradient-left');
        expect(lastQuiz.classes()).toContain('apply-gradient-right');
    });



});

describe('calculateVisibleCount based on window width', () => {
    let wrapper;

    // Helper function to update window width and trigger resize event
    const setWindowWidth = (width) => {
        global.innerWidth = width;
        window.dispatchEvent(new Event('resize'));
    };

    beforeEach(() => {
        wrapper = mount(PublicProfileComponent, {
            props: {
                user: { profilePicture: 'https://via.placeholder.com/100', username: 'JohnDoe' },
                quizzes: new Array(10).fill(0).map((_, index) => ({ id: index })), // Mock 10 quizzes
            },
            global: {
                stubs: { QuizCard: true }, // Stub the QuizCard component
            },
        });
    });

    it('shows 5 items for extra large desktop widths', async () => {
        setWindowWidth(1920);
        await wrapper.vm.$nextTick();
        expect(wrapper.findAll('.carousel-slide').length).toBe(5);
    });

    it('shows 4 items for large desktop widths', async () => {
        setWindowWidth(1440);
        await wrapper.vm.$nextTick();
        expect(wrapper.findAll('.carousel-slide').length).toBe(4);
    });

    it('shows 3 items for desktop widths', async () => {
        setWindowWidth(1024);
        await wrapper.vm.$nextTick();
        expect(wrapper.findAll('.carousel-slide').length).toBe(3);
    });

    it('shows 2 items for tablet widths', async () => {
        setWindowWidth(768);
        await wrapper.vm.$nextTick();
        expect(wrapper.findAll('.carousel-slide').length).toBe(2);
    });

    it('shows 1 item for mobile widths', async () => {
        setWindowWidth(767); // Assuming anything less than 768 is considered mobile
        await wrapper.vm.$nextTick();
        expect(wrapper.findAll('.carousel-slide').length).toBe(1);
    });
});

