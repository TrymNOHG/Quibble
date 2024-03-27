<template>
  <div class="author-list">
    <ul>
      <li v-for="(author, index) in authors" :key="index">
        <div class="author-info">
          <router-link to="/userprofile" class="author-link">
            <span>{{ author }}</span>
          </router-link>
          <div class="actions">
            <!--<font-awesome-icon id="pen" icon="fa-solid fa-pen-to-square" @click="editAuthor(author)"/>-->
            <font-awesome-icon id="trash" icon="fa-solid fa-trash" @click="deleteAuthor(author)" />
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  authors: {
    type: Array,
    required: true
  }
});

const editAuthor = (author) => {
  // Handle edit action
};

const deleteAuthor = (author) => {
  // Handle delete action
};
</script>

<style scoped>
.author-list {
  margin-top: 5%;
  width: 100%;
}

.author-list ul {
  list-style-type: none;
  padding: 0;
}

.author-list li {
  width: 100%;
  color: white;
  text-align: center;
  margin-bottom: 3%;
  justify-content: space-between;
  align-content: center;
  background-color: rgba(19, 155, 250, 0.88);
  height: 60px;
  border-radius: 10px;
}

.author-info {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  width: 100%;
}

span {
  font-weight: bold;
}

.author-link {
  text-decoration: none;
  color: white;
}

.actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 50%;
  cursor: pointer;
}

#pen {
  margin-right: 25%;
}

#pen:hover {
  scale: 1.25;
  color: lawngreen;
  cursor: pointer;
}

#trash:hover {
  scale: 1.25;
  color: #ff001e;
  cursor: pointer;
}
</style>
