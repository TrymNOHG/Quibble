<template>
  <div>
    <private-profile-component
        @updateUserProfile="handleUpdateUserProfile"
        @updatePassword="handleUpdatePassword"
        @logout="handleLogout"
        @changeProfilePicture="handleChangePicture"
        @deleteProfilePicture="handleDeletePicture"
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
import {deleteQuizById} from "@/services/QuizService.js";
import {getPictureFromID} from "@/services/ImageService.js";

export default {
  name: "ProfileView",
  components: { PrivateProfileComponent },

  methods: {
    loadUserData() {
      // useUserStore().fetchUserData()
      console.log(useUserStore().getUserData)
      return useUserStore().getUserData
    },
    async handleUpdateUserProfile(userData) {
      const store = useUserStore();
      const user = store.getUserData;
      console.log(userData)

      let userUpdateDTO = new FormData();
      userUpdateDTO.append('userId', user.userId);
      userUpdateDTO.append('username', userData.username);
      userUpdateDTO.append('showActivity', user.showActivity);
      userUpdateDTO.append('showFeedback', user.showFeedback);

      await updateUser(userUpdateDTO)
          .then(response => {
            console.log(response)
          }).catch(error => {
            console.warn("error", error)
          })
      await store.fetchUserData();
      await router.push("/profile");
    },

    handleUpdatePassword(passwordData) {
      console.log("Updating password with:", passwordData);
      // Implement logic here to update the password
    },

    handleLogout() {
      useUserStore().logoutUser()
      router.push("/");
    },

    handleChangePicture(file) {
      let userUpdateDTO = new FormData();
      userUpdateDTO.append('userId', useUserStore().user.userId);
      userUpdateDTO.append('profilePicture', file);
      updateUser(userUpdateDTO).then(r => {
        useUserStore().fetchUserData()
      }).catch(e => {
        //TODO: handle error.
      });

    },

    getPictureURL() {
      const id =`Q${quiz.quizId}`
      return getPictureFromID(id);
    },

    handleDeletePicture(pictureUrl) {
      console.log("Deleting picture:", pictureUrl);
      // Implement your logic here to delete the profile picture
    },

    handleToggleEdit(editState) {
      console.log("Toggling edit state to:", editState);
      this.isEditing = editState; // Ensure this line is correctly updating the isEditing state
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
      router.push("/");
      console.log("Deleting user account");
      // Implement your logic here to delete the user account
    },

  },
};
</script>


<style scoped>

</style>