<template>
  <div class="popup" v-if="showPopupProp" @click.self="closePopup">
    <div class="popup-content">
      <h3>Do you want to delete this?</h3>
      <div class="button-group">
        <button @click="deleteItem">Yes</button>
        <button @click="closePopup">No</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  showPopupProp: Boolean
});

const emits = defineEmits(['update:showPopupProp']);

const deleteItem = () => {
  console.log('Item deleted');
  closePopup();
};

const closePopup = () => {
  emits('update:showPopupProp', false);
};
</script>

<style scoped>
.popup {
  position: absolute;
  top: 0;
  left: 0;
  transform: translate(-300%, -100%);
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.popup-content {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 5px;
}

.button-group {
  display: flex;
  justify-content: space-evenly;
  margin-top: 10px;
}

.button-group button {
  width: 100px;
}
</style>
