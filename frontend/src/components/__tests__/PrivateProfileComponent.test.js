// Import necessary utilities from Vitest and Vue Test Utils
import { describe, it, expect, beforeEach } from 'vitest';
import { mount} from '@vue/test-utils';
import PrivateProfileComponent from '@/components/Profile/PrivateProfileComponent.vue';
import BasicButton from "@/components/BasicComponents/basic_button.vue";

describe('UserProfile Defaults', () => {
    it('uses default values when no props are provided', () => {
        const wrapper = mount(PrivateProfileComponent, {
            global: {
                components: { BasicButton },
                mocks: { $t: msg => msg }, // Localization mock
                stubs: { 'font-awesome-icon': true } // Stub out external components
            }
        });


        // Since profileData is an object, we need to access its properties through the component's instance
        // Adjust these assertions based on how you access the props in your component
        expect(wrapper.vm.profileData.username).toBe('johndoe');
        expect(wrapper.vm.profileData.email).toBe('john@doe.org');
        expect(wrapper.vm.profileData.profilePicture).toBe('https://placehold.co/600x400');
    });
});

// Define the describe block for the PrivateProfileComponent component tests
describe('PrivateProfileComponent', () => {
    let wrapper;
    const profileData = {
        username: 'janedoe',
        email: 'jane@example.com',
        picture: 'https://placehold.co/600x400',
    };

    // Setup function runs before each test case
    beforeEach(() => {
        wrapper = mount(PrivateProfileComponent, {
            props: { profileData },
            global: {
                components: { BasicButton },
                mocks: { $t: msg => msg }, // Localization mock
                stubs: { 'font-awesome-icon': true } // Stub out external components
            }
        });
    });

    /*
    // Test for rendering user data
    it('renders user data correctly', () => {
        expect(wrapper.text()).toContain(profileData.username);
        expect(wrapper.text()).toContain(profileData.email);
        expect(wrapper.find('.profile-picture').attributes('src')).toBe(profileData.picture);
    });
    */

    // Test for edit mode toggling
    it('toggles edit mode when edit button is clicked', async () => {
        await wrapper.find('.edit-btn').trigger('click');
        expect(wrapper.vm.isEditing).toBe(true);
    });

    // Test for logout event emission
    it('emits logout event when logout button is clicked', async () => {
        await wrapper.find('.logout-btn').trigger('click');
        expect(wrapper.emitted()).toHaveProperty('logout');
    });
    
    // Test for toggling show activity
    it('toggles show activity status', async () => {
        const checkbox = wrapper.find('#toggle-activity');
        await checkbox.setChecked(!checkbox.element.checked); // Toggle checkbox
        expect(wrapper.vm.showActivity).toBe(checkbox.element.checked); // Check if internal value matches
    });

    // Test for toggling feedback visibility on profile
    it('toggles show feedback on profile status', async () => {
        const checkbox = wrapper.find('#toggle-feedback');
        await checkbox.setChecked(!checkbox.element.checked); // Toggle checkbox
        expect(wrapper.vm.showFeedbackOnProfile).toBe(checkbox.element.checked); // Check if internal value matches
    });

});
