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
        expect(wrapper.vm.profileData.firstName).toBe('john');
        expect(wrapper.vm.profileData.lastName).toBe('doe');
        expect(wrapper.vm.profileData.username).toBe('johndoe');
        expect(wrapper.vm.profileData.email).toBe('john@doe.org');
        expect(wrapper.vm.profileData.picture).toBe('https://placehold.co/600x400');
    });
});

// Define the describe block for the PrivateProfileComponent component tests
describe('PrivateProfileComponent', () => {
    let wrapper;
    const profileData = {
        firstName: 'Jane',
        lastName: 'Doe',
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

    // Test for rendering user data
    it('renders user data correctly', () => {
        expect(wrapper.text()).toContain(profileData.firstName);
        expect(wrapper.text()).toContain(profileData.lastName);
        expect(wrapper.text()).toContain(profileData.username);
        expect(wrapper.text()).toContain(profileData.email);
        expect(wrapper.find('.profile-picture').attributes('src')).toBe(profileData.picture);
    });

    // Test for edit mode toggling
    it('toggles edit mode when edit button is clicked', async () => {
        await wrapper.find('.edit-btn').trigger('click');
        expect(wrapper.vm.isEditing).toBe(true);
    });

    // Test for change password mode toggling
    it('toggles change password mode when change password button is clicked', async () => {
        await wrapper.find('.change-password-btn').trigger('click');
        expect(wrapper.vm.isChangingPassword).toBe(true);
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


// Test for deleting profile picture
    it('emits deletePicture event when delete icon is clicked', async () => {
        await wrapper.find('.deleteIcon').trigger('click');
        expect(wrapper.emitted()).toHaveProperty('deletePicture');
    });


    // Test for handling default profile picture
    it('displays default picture when no picture is provided', () => {
        wrapper.setProps({
            profileData: { ...wrapper.props().profileData, picture: '' }
        });
        expect(wrapper.vm.defaultPicture).toBeTruthy(); // Checks if the computed property returns a truthy value
        expect(wrapper.find('.profile-picture').attributes('src')).toBe(wrapper.vm.defaultPicture); // Checks if the default picture is being used
    });



    // Test `onPictureChange` method
    it('emits changePicture event on picture change', async () => {
        // Mock the file to be uploaded
        const file = new File(['dummy content'], 'test.png', { type: 'image/png' });
        // Create a mock event with the file as the target's files list
        const mockChangeEvent = { target: { files: [file] } };
        // Trigger the onPictureChange method directly with the mock event
        wrapper.vm.onPictureChange(mockChangeEvent);
        // Check if the changePicture event was emitted with the expected payload
        expect(wrapper.emitted().changePicture[0]).toEqual([file]);
    });

    it('sets isEditing to true after clicking the edit button', async () => {
        await wrapper.find('.edit-btn').trigger('click');
        await wrapper.vm.$nextTick(); // Ensure Vue has processed the DOM update

        // Directly assert the state change
        expect(wrapper.vm.isEditing).toBe(true);
    });

    it('sets isChangingPassword to true after clicking the change password button', async () => {
        await wrapper.find('.change-password-btn').trigger('click');
        await wrapper.vm.$nextTick(); // Ensure Vue has processed the DOM update

        // Directly assert the state change
        expect(wrapper.vm.isChangingPassword).toBe(true);
    });


    // Test for profile update event emission
    it('emits updateUserProfile event with correct payload on form submission', async () => {
        // Switch to editing mode
        await wrapper.find('.edit-btn').trigger('click');

        // Set up new profile data
        const updatedProfile = {
            firstName: 'UpdatedName',
            lastName: 'UpdatedLast',
            username: 'updatedUsername',
            email: 'updated@example.com'
        };

        // Update input fields
        await wrapper.find('#firstName').setValue(updatedProfile.firstName);
        await wrapper.find('#lastName').setValue(updatedProfile.lastName);
        await wrapper.find('#username').setValue(updatedProfile.username);
        await wrapper.find('#email').setValue(updatedProfile.email);

        // Submit form
        await wrapper.vm.submitForm();

        // Check if event with updated profile data is emitted
        expect(wrapper.emitted().updateUserProfile[0][0]).toEqual(updatedProfile);
    });



    it('emits updateUserProfile event with correct payload on form submission', async () => {
        // Switch to editing mode
        await wrapper.find('.edit-btn').trigger('click');

        // Set up new profile data
        const updatedProfile = {
            firstName: '',
            lastName: '',
            username: '',
            email: ''
        };

        // Update input fields
        await wrapper.find('#firstName').setValue(updatedProfile.firstName);
        await wrapper.find('#lastName').setValue(updatedProfile.lastName);
        await wrapper.find('#username').setValue(updatedProfile.username);
        await wrapper.find('#email').setValue(updatedProfile.email);

        // Submit form
        await wrapper.vm.submitForm();

        // Check if event with updated profile data is emitted
        expect(wrapper.emitted().updateUserProfile).toBeFalsy();;
    });

// Test for password update event emission
    it('emits updatePassword event with correct payload on password form submission', async () => {
        // Switch to changing password mode
        await wrapper.find('.change-password-btn').trigger('click');

        // Set up new password data
        const newPasswordData = {
            oldPassword: 'oldPassword123',
            newPassword: 'newPassword123',
            confirmPassword: 'newPassword123'
        };

        // Update input fields
        await wrapper.find('#currentPassword').setValue(newPasswordData.oldPassword);
        await wrapper.find('#newPassword').setValue(newPasswordData.newPassword);
        await wrapper.find('#confirmPassword').setValue(newPasswordData.confirmPassword);

        // Simulate form submission by calling the method directly
        await wrapper.vm.emitUpdatePassword();

        // Check if event with new password data is emitted
        expect(wrapper.emitted().updatePassword[0][0]).toEqual({
            confirmPassword: newPasswordData.confirmPassword,
            oldPassword: newPasswordData.oldPassword,
            newPassword: newPasswordData.newPassword
        });
    });

    it('fails to update password due to validation error', async () => {
        await wrapper.find('.change-password-btn').trigger('click');


        const newPasswordData = {
            oldPassword: 'oldPassword123',
            newPassword: 'newPasswor',
            confirmPassword: 'newPassword123'
        };

        // Update input fields
        await wrapper.find('#currentPassword').setValue(newPasswordData.oldPassword);
        await wrapper.find('#newPassword').setValue(newPasswordData.newPassword);
        await wrapper.find('#confirmPassword').setValue(newPasswordData.confirmPassword);

        // Simulate form submission by calling the method directly
        await wrapper.vm.emitUpdatePassword();



        await wrapper.vm.emitUpdatePassword();

        // Check that the validation error was caught and logged
        expect(wrapper.emitted().updatePassword).toBeFalsy(); // Ensure no updatePassword event was emitted

    });




    // Additional tests can include checking for the presence or absence of certain UI elements based on state
    // For instance, ensuring certain input fields are present when in edit mode, but not otherwise
});
