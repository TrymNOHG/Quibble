<template>
  <div>
    <div class="user-display">
      {{ userData.username }}
      <font-awesome-icon
          id="add"
          icon="fa-solid fa-plus"
          @click="addAuthor"
      />
    </div>
    <div v-if="userData === null" class="empty-list">
      No user found.
    </div>

  </div>
</template>

<script>
import {getCurrentInstance} from "vue";

export default {
  props: {
    userData: {
      type: Object,
      default: () => ({
        email: String,
        profilePicture: String,
        showActivity: false,
        showFeedback: false,
        userId: Number,
        username: String,
      })
    }
  },

  setup() {
    const { emit } = getCurrentInstance();

    const addAuthor = (userData) => {
      emit("adduser", userData);
    };

    return {
      addAuthor,
    }
  }
};
</script>

<style scoped>
#add {
  scale: 1.2;
  margin-right: 0;
}

#add:hover {
  scale: 1.5;
  cursor: pointer;
  color: #71da11;
}


.user-display {
  list-style-type: none;
  margin-bottom: 5px;
  padding: 5px 10px;
  background-color: #f0f0f0;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-list {
  color: gray;
}
</style>
