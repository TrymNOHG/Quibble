<template>
  <div>
    <private-profile-component
        @updateUserProfile="handleUpdateUserProfile"
        @logout="handleLogout"
        @changeProfilePicture="handleChangePicture"
        @deleteProfilePicture="handleDeletePicture"
        @toggleEdit="handleToggleEdit"
        @updateShowActivity="handleUpdateShowActivity"
        @updateShowFeedbackOnProfile="handleUpdateShowFeedbackOnProfile"
        :profile-data="loadUserData()"
    />
  </div>
</template>




<script>
import PrivateProfileComponent from "@/components/Profile/PrivateProfileComponent.vue";
import {useUserStore} from "@/stores/counter.js";
import {updateUser} from "@/services/UserService.js";
import router from "@/router/index.js";
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
      if (userData.username !== null) {
        userUpdateDTO.append('username', userData.username);
      }
      userUpdateDTO.append('email', user.email);
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
      }).catch(e => {});
    },

    getPictureURL() {
      const id =`Q${quiz.quizId}`
      return getPictureFromID(id);
    },

    handleDeletePicture(pictureUrl) {
      console.log("Deleting picture:", pictureUrl);
    },

    handleToggleEdit(editState) {
      console.log("Toggling edit state to:", editState);
      this.isEditing = editState;
    },

    handleUpdateShowActivity(showActivity) {
      console.log("Updating show activity to:", showActivity);
      let userUpdateDTO = {
        'userId' : useUserStore().user.userId,
        'showActivity' : showActivity,
      }
      updateUser(userUpdateDTO).then(r => {
        useUserStore().setShowActivity(showActivity)
      }).catch(e => {});
    },

    handleUpdateShowFeedbackOnProfile(showFeedback) {
      console.log("Updating show feedback on profile to:", showFeedback);
      let userUpdateDTO = {
          'userId' : useUserStore().user.userId,
          'showFeedback' : showFeedback
      }
      updateUser(userUpdateDTO).then(r => {
        useUserStore().setShowFeedback(showFeedback)
      }).catch(e => {});
    },
  },
};
</script>


<style scoped>

</style>