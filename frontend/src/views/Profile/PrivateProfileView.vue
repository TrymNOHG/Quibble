<template>
  <div>
    <private-profile-component
        @updateUserProfile="handleUpdateUserProfile"
        @updatePassword="handleUpdatePassword"
        @logout="handleLogout"
        @changePicture="handleChangePicture"
        @deletePicture="handleDeletePicture"
        @toggleEdit="handleToggleEdit"
        @toggleChangePassword="handleToggleChangePassword"
        @updateShowActivity="handleUpdateShowActivity"
        @updateShowFeedbackOnProfile="handleUpdateShowFeedbackOnProfile"
    />
  </div>
</template>

<script>
import PrivateProfileComponent from "@/components/Profile/PrivateProfileComponent.vue"; // Ensure this matches your imported component file name

export default {
  name: "ProfileView",
  components: { PrivateProfileComponent },

  methods: {
    handleUpdateUserProfile(userData) {
      console.log("Updating user profile with:", userData);
      // Implement your logic here to update the user profile
    },

    handleUpdatePassword(passwordData) {
      console.log("Updating password with:", passwordData);
      // Implement your logic here to update the password
    },

    handleLogout() {
      console.log("Logging out");
      // Implement your logout logic here
    },

    handleChangePicture(file) {
      console.log("Changing picture to:", file.name);
      // Implement your logic here to change the profile picture
    },

    handleDeletePicture(pictureUrl) {
      console.log("Deleting picture:", pictureUrl);
      // Implement your logic here to delete the profile picture
    },

    handleToggleEdit(editState) {
      console.log("Toggling edit state to:", editState);
      // Additional logic can be implemented if needed
    },

    handleToggleChangePassword(changePasswordState) {
      console.log("Toggling change password state to:", changePasswordState);
      // Additional logic can be implemented if needed
    },

    handleUpdateShowActivity(showActivity) {
      console.log("Updating show activity to:", showActivity);
      // Implement your logic here to update the activity visibility
    },

    handleUpdateShowFeedbackOnProfile(showFeedback) {
      console.log("Updating show feedback on profile to:", showFeedback);
      // Implement your logic here to update the feedback visibility
    },
  },
};
</script>


<style scoped>

</style>