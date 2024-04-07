import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import ScoreComponent from "@/components/QuizPlaing/scoreComponent.vue"; // Adjust the path as needed
import { getPictureFromID } from "@/services/ImageService.js";

// Mocking the ImageService
vi.mock("@/services/ImageService.js", () => ({
    getPictureFromID: vi.fn((id) => `mocked_url_for_${id}`),
}));

// Mocking useI18n globally if needed or use local mocks
describe('ScoreComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(ScoreComponent, {
            props: {
                players: [
                    { username: 'Player1', score: 100, imageId: 'img1' },
                    { username: 'Player2', score: 200, imageId: 'img2' }
                ],
                gameEnded: false,
            },
            global: {
                mocks: {
                    t: (msg) => msg, // Providing a mock for the t function from vue-i18n
                },
            },
        });
    });

    it('renders player scores correctly', () => {
        const scoreItems = wrapper.findAll('.score-item');
        expect(scoreItems.length).toBe(2);
        expect(scoreItems[0].text()).toContain('Player1');
        expect(scoreItems[1].text()).toContain('200'); // Player 2's score
    });

    it('displays the countdown timer when game is not ended', () => {
        expect(wrapper.text()).toContain('ScoresTid igjen:  51Player11002Player2200');
    });

    it('updates the countdown timer every second', async () => {
        vi.useFakeTimers();
        expect(wrapper.vm.countdown).toBe(5);
        vi.advanceTimersByTime(3000); // Simulate 3 seconds
        await wrapper.vm.$nextTick(); // Wait for the DOM to update
        expect(wrapper.text()).toContain('ScoresTid igjen:  51Player11002Player2200');
        vi.useRealTimers();
    });

    it('shows fireworks when the game has ended', async () => {
        await wrapper.setProps({ gameEnded: true });
        expect(wrapper.find('.fireworks').exists()).toBe(true);
    });

    it('calls getPictureFromID to generate player image URLs', () => {
        expect(getPictureFromID).toHaveBeenCalledWith('img1');
        expect(wrapper.find('.player-image').attributes('src')).toContain('mocked_url_for_img1');
    });

    afterEach(() => {
        vi.restoreAllMocks(); // Reset mocks after each test
    });
});
