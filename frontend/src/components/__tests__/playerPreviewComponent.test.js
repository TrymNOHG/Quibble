import { describe, it, expect, beforeEach, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import playerPreviewComponent from "@/components/QuizPlaing/playerPreviewComponent.vue";
import { getPictureFromID } from "@/services/ImageService.js";

// Mocking the ImageService
vi.mock('@/services/ImageService.js', () => ({
    getPictureFromID: vi.fn((id) => `https://example.com/${id}.png`),
}));

// Mocking Vue I18n
vi.mock('vue-i18n', () => ({
    useI18n: () => ({
        t: vi.fn((key) => key),
    }),
}));

const mockPlayers = [
    { username: 'PlayerOne', profilePicture: 'pic1' },
    { username: 'PlayerTwo', profilePicture: 'pic2' },
];

describe('PlayerPreviewComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(playerPreviewComponent, {
            props: {
                players: mockPlayers,
            },
        });
    });

    it('renders the correct number of player cards', () => {
        const playerCards = wrapper.findAll('.player-card');
        expect(playerCards).toHaveLength(mockPlayers.length);
    });

    it('displays player usernames and avatars correctly', () => {
        mockPlayers.forEach((player, index) => {
            const playerCard = wrapper.findAll('.player-card')[index];
            expect(playerCard.text()).toContain(player.username);
            const avatar = playerCard.find('.player-avatar');
            expect(avatar.attributes('src')).toBe(`https://example.com/${player.profilePicture}.png`);
        });
    });

    it('uses internationalization for the title', () => {
        const title = wrapper.find('h2');
        expect(title.text()).toBe('quiz_playing.player_preview');
    });

    // Add more tests as needed
});
