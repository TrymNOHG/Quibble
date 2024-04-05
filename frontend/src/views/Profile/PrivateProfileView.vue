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
        @deleteUser="handleDeleteUser"
        :profile-data="loadUserData()"
    />
  </div>
</template>




<script>
import PrivateProfileComponent from "@/components/Profile/PrivateProfileComponent.vue";
import {useUserStore} from "@/stores/counter.js"; // Ensure this matches your imported component file name
import {updateUser, updateUserShowActivity, updateUserShowFeedback} from "@/services/UserService.js";
import router from "@/router/index.js";

export default {
  name: "ProfileView",
  components: { PrivateProfileComponent },

  methods: {
    loadUserData() {
      console.log(useUserStore().getUserData)
      return useUserStore().getUserData
    },
    handleUpdateUserProfile(userData) {
      console.log("Updating user profile with:", userData);
      // Implement your logic here to update the user profile
    },

    handleUpdatePassword(passwordData) {
      console.log("Updating password with:", passwordData);
      // Implement your logic here to update the password
    },

    handleLogout() {
      useUserStore().logoutUser()
      router.push("/");
    },

    handleChangePicture(file) {
      console.log("Changing picture to:", file.name);
      let userUpdateDTO = {
        'userId' : useUserStore().user.userId,
        'profilePicture' : file,
      }
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
      let userUpdateDTO = {
        'userId' : useUserStore().user.userId,
        'showActivity' : showActivity,
      }
      updateUser(userUpdateDTO).then(r => {
        useUserStore().setShowActivity(showActivity)
      }).catch(e => {
        //TODO: handle error.
      });
    },

    handleUpdateShowFeedbackOnProfile(showFeedback) {
      console.log("Updating show feedback on profile to:", showFeedback);
      let userUpdateDTO = {
          'userId' : useUserStore().user.userId,
          'showFeedback' : showFeedback
      }
      updateUser(userUpdateDTO).then(r => {
        useUserStore().setShowFeedback(showFeedback)
      }).catch(e => {
        //TODO: handle error.
      });
    },

    handleDeleteUser() {
      outer.push("/");
      console.log("Deleting user account");
      // Implement your logic here to delete the user account
    },

  },
};
</script>


<style scoped>

</style>