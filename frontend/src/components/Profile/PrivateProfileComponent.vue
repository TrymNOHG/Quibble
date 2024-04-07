<template>
  <div class="user-profile">
    <div class="profile-profilePicture-container">
      <img :src="profileData.profilePicture" alt="Profile profilePicture" class="profile-profilePicture" />
      <div class="profile-profilePicture-overlay">
        <label for="fileInput" style="cursor: pointer;">
          <img class="editIcon" src="@/assets/images/edit.svg" alt="Change">
        </label>
        <input id="fileInput" type="file" @change="onProfilePictureChange" style="display: none;"
               accept="image/jpeg, image/png"/>
        <img
            v-show="profileData.profilePicture !== defaultProfilePicture"
            class="deleteIcon"
            src="@/assets/images/delete-icn.svg"
            alt="Delete"
            @click="onDeleteProfilePicture"
        />
      </div>
    </div>

    <div v-if="!isEditing" class="info-section">
<!--      <p id="fullName"><strong>{{ $t("name") }}:</strong> {{ profileData.firstName + " " + profileData.lastName }}</p>-->
      <p id="username"><strong>{{ $t("Username") }}:</strong> {{ profileData.username }}</p>
      <p id="email"><strong>{{ $t("Email") }}:</strong> {{ profileData.email }}</p>
      <div class="toggle-container">
        <label for="toggle-activity">Show Activity:</label>
        <input id="toggle-activity" type="checkbox" v-model="showActivity" class="toggle-input" />
      </div>
      <div class="toggle-container">
        <label for="toggle-feedback">Show Feedback on Profile:</label>
        <input id="toggle-feedback" type="checkbox" v-model="showFeedbackOnProfile" class="toggle-input" />
      </div>
      <BasicButton class="basic-button edit-btn" @click="toggleEdit(true)" :button_text="$t('edit')"/>
      <BasicButton class="basic-button logout-btn" @click="logout" :button_text="$t('Logout')"/>

    </div>

    <form v-else-if="isEditing" @submit.prevent="submitForm" :class="{ 'has-errors': Object.keys(errors).length > 0 }">
<!--      <div class="input-box">-->
<!--        <label for="firstName">First Name</label>-->
<!--        <input id="firstName" v-model="firstName" type="text" required />-->
<!--        <div v-if="errors.firstName" class="error-message">{{ errors.firstName }}</div>-->
<!--      </div>-->
<!--      <div class="input-box">-->
<!--        <label for="lastName">Last Name</label>-->
<!--        <input id="lastName" v-model="lastName" type="text" required />-->
<!--        <div v-if="errors.lastName" class="error-message">{{ errors.lastName }}</div>-->
<!--      </div>-->
      <!-- Repeat for username and email fields -->
      <div class="input-box">
        <label for="username">Username</label>
        <input id="username" v-model="username" type="text" required />
        <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
      </div>
      <div class="input-box">
        <label for="email">Email</label>
        <input id="email" v-model="email" type="email" required />
        <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
      </div>
      <div class="button-container">
        <BasicButton class="basic-button" type="submit" @click="submitForm" :button_text="'Save Changes'"/>
        <BasicButton class="basic-button" type="button" @click="toggleEdit(false)" :button_text="'Cancel'"/>
      </div>
    </form>
  </div>
</template>


<script>
import BasicButton from "@/components/BasicComponents/basic_button.vue";
import {ref, watch, computed, toRefs} from 'vue';
import * as yup from "yup";
import { useField, useForm } from "vee-validate";


export default {
  name: 'UserProfile',
  components: { BasicButton },
  props: {
    profileData: {
      type: Object,
      required: true,
      default: () => ({
        username: 'johndoe',
        email: 'john@doe.org',
        profilePicture: 'https://placehold.co/600x400',
        showActivity: false,
        showFeedback: false
      }),
    }
  },
  setup(props, { emit }) {
    // State variables
    const { profileData } = toRefs(props);
    const isEditing = ref(false);
    const showActivity = ref(profileData.value.showActivity);
    const showFeedbackOnProfile = ref(profileData.value.showFeedback);
    const hasErrors = ref(false);
    const passwordData = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
    });
    const defaultProfilePicture = computed(() => 'https://placehold.co/600x400');

    // Form fields managed by vee-validate
    const validationSchema = yup.object({
      // firstName: yup.string().required("First name is required"),
      // lastName: yup.string().required("Last name is required"),
      username: yup.string().required("Username is required"),
      email: yup.string().required("Email is required").email("Must be a valid email"),
    });

    const { handleSubmit, errors } = useForm({
      validationSchema,
      initialValues: {
        username: profileData.value.username,
        email: profileData.value.email,
      }
    });

    const { value: username } = useField('username', yup.string().required("Username is required"), { initialValue: profileData.value.username });
    const { value: email } = useField('email', yup.string().required("Email is required").email("Must be a valid email"), { initialValue: profileData.value.email });

    // Form submission handling
    const submitForm = handleSubmit(values => {
      emit('updateUserProfile', values);
      toggleEdit();
    });

    // Watchers for reactive properties
    watch(showActivity, newValue => emit('updateShowActivity', newValue));
    watch(showFeedbackOnProfile, newValue => emit('updateShowFeedbackOnProfile', newValue));

    // Methods for component functionality
    function toggleEdit(editState) {
      isEditing.value = editState;
      emit('toggleEdit', editState);
    }

    function logout() {
      emit('logout');

    }

    function onProfilePictureChange(event) {
      const file = event.target.files[0];
      if (file) emit('changeProfilePicture', file);
    }

    function onDeleteProfilePicture() {
      emit('deleteProfilePicture', props.profileData.profilePicture);
    }

    return {
      hasErrors,
      defaultProfilePicture,
      isEditing,
      passwordData,
      showActivity,
      showFeedbackOnProfile,
      submitForm,
      logout,
      toggleEdit,
      onProfilePictureChange,
      onDeleteProfilePicture,
      username,
      email,
      errors,
    };
  },
};
</script>






<style scoped>

.toggle-container {
  display: flex;
  align-items: center;
  margin: 1rem 0;
}

.toggle-container label {
  margin-right: 1rem;
}

.toggle-input {
  -webkit-appearance: none;
  appearance: none;
  width: 3rem;
  height: 1.5rem;
  background: #ddd;
  border-radius: 1.5rem;
  position: relative;
  outline: none;
  cursor: pointer;
  transition: background 0.1s ease-in-out;
}

.toggle-input:checked {
  background: #007bff;
}

.toggle-input:before {
  content: '';
  position: absolute;
  top: 0.25rem;
  left: 0.25rem;
  width: 1rem;
  height: 1rem;
  background: #fff;
  border-radius: 1rem;
  transition: transform 0.1s ease-in-out;
}


.toggle-input:checked:before {
  transform: translateX(1.5rem);
}


.user-profile {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
  background-color: #f8f8f8;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
}

.profile-profilePicture-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 1rem;
}

.profile-profilePicture {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-profilePicture-overlay {
  position: absolute;
  width: 150px;
  height: 150px;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
  cursor: pointer;
}

.profile-profilePicture-overlay img {
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.profile-profilePicture-overlay img.editIcon {
  filter: invert(100%) sepia(100%) saturate(0%) hue-rotate(300deg) brightness(100%) contrast(100%);
}

.profile-profilePicture-overlay img.deleteIcon {
  filter: brightness(0) saturate(100%) invert(100%) sepia(100%) saturate(0%) hue-rotate(300deg) brightness(100%) contrast(100%);
}

.profile-profilePicture-container:hover .profile-profilePicture-overlay,
.profile-profilePicture-overlay:focus {
  opacity: 1;
}

.profile-profilePicture-overlay input[type="file"] {
  display: none;
}

.info-section {
  display: grid;
  gap: 0.5rem;
}

.user-profile p {
  font-size: 1.1rem;
  margin-bottom: 0.25rem;
}

.user-profile form {
  display: grid;
  gap: 0.5rem;
}

.user-profile form label {
  font-size: 1.1rem;
  display: block;
  margin-bottom: 0.25rem;
}



.input-box {
  position: relative;
  margin-bottom: 0.5rem;
}

.input-box input {
  background-color: transparent;
  border: none;
  border-bottom: 2px solid black;
  outline: none;
  width: 100%;
  padding: 0.5rem 0;
  font-size: 1rem;
}

.input-box label {
  font-size: 1.1rem;
  display: block;
  margin-bottom: 0.25rem;
}

.input-box .icon {
  position: absolute;
  top: calc(50% + 21px);
  right: 0;
  transform: translateY(-50%);
  pointer-events: none;
}

.button-container {
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;
}

button {
  width: 100%;
  height: 40px;
  background: #181818;
  border: none;
  outline: none;
  border-radius: 5px;
  cursor: pointer;
  color: white;
}

@media (max-width: 768px) {
  .user-profile {
    padding: 1rem;
  }
}

@media (max-width: 500px) {
  h1 {
    font-size: 1.5rem;
  }

  .profile-profilePicture {
    width: 100px;
    height: 100px;
  }

  .profile-profilePicture-overlay {
    width: 100px;
    height: 100px;
  }
}
</style>