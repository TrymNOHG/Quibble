import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import scoreComponentOnePlayer from "@/components/QuizPlaing/scoreComponentOnePlayer.vue";
import { getPictureFromID } from "@/services/ImageService.js";

// Mock external dependencies
vi.mock('@/services/ImageService.js', () => ({
    getPictureFromID: vi.fn((id) => `https://example.com/${id}.png`),
}));

vi.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: vi.fn((key) => key),
    }),
}));

const mockPlayer = {
    userId: 'user1',
    username: 'PlayerOne',
};

describe('ScoreComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(scoreComponentOnePlayer, {
            props: {
                player: mockPlayer,
                score: 100,
            },
        });
    });

    it('renders player information and score correctly', () => {
        expect(wrapper.text()).toContain(mockPlayer.username);
        expect(wrapper.text()).toContain('100');
        const img = wrapper.find('.player-image');
        expect(img.attributes('src')).toBe(`https://example.com/${mockPlayer.userId}.png`);
    });

    it('starts countdown on mount', async () => {
        vi.useFakeTimers();
        await vi.advanceTimersByTime(5000); // Fast-forward 5 seconds
        expect(wrapper.vm.countdown).toBe(5);
        vi.restoreAllMocks(); // Clean up timers
    });

    it('emits countdownEnded event after countdown', async () => {
        vi.useFakeTimers();
        wrapper.vm.startCountdown(); // Ensure countdown is started
        await vi.advanceTimersByTime(5000); // Fast-forward 5 seconds
        expect(wrapper.emitted()).toHaveProperty('countdownEnded');
        vi.restoreAllMocks(); // Clean up timers
    });

    // Additional tests can include checking for dynamic styling based on the player's score,
    // verifying internationalization for static text, and other interactive behaviors.
});
