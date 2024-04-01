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
        :profile-data="loadUserData()"
    />
  </div>
</template>

<script>
import PrivateProfileComponent from "@/components/Profile/PrivateProfileComponent.vue";
import {useUserStore} from "@/stores/counter.js"; // Ensure this matches your imported component file name
export default {
  name: "ProfileView",
  components: { PrivateProfileComponent },

  methods: {
    loadUserData() {
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

.grey-bar {
  background-color: #6C6C6C;

  text-align: center;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

#grey-header{
  grid-column: 2;
  color: white;
}

.information-button{
  display: flex;
  grid-column: 3;
  text-align: right;
  margin-left: auto;
}

#info-picture{
  height: 30px;
  width: 30px;
  cursor: pointer;

}

@media only screen and (min-width: 10px) and (max-width: 650px) {

  .grey-bar {
    display: flex;
    align-content: center;
    justify-content: center;
    margin-top: 5px;
    background-color: #31c48d;
    max-height: 60px;
    height: 60px;
    border-radius: 20px 20px 0 0;
  }



  #grey-header {
    display: flex;
    grid-template-columns: 1fr 1fr;
    grid-column-gap: 10px;
    grid-column: 2;
    text-align: center;

    justify-content: center;
    margin-left: 30%;
    height: 60px !important;
    background-color: white;
    font-size: 20px;
    border-radius: 20px 20px 0 0;
    font-weight: bold;
    text-decoration: none;
    text-shadow: none;
    color: black;
    margin-top: 20px;
    padding-top: 5px;
    padding-right: 5px;
    padding-left: 5px;
    width: 40%;
  }

  #info-picture{
    height: 30px;
    width: 30px;
    top: 0;
  }

  .information-button {
    display: flex;
    margin-left: auto;
    margin-right: 8px;
    gap: 30%;
    left: 0;
    height: 60px;
  }
}

</style>