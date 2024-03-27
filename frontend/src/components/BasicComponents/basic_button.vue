<template>
  <div
      class="button"
  >
    {{button_text}}
  </div>
</template>

<script>
export default {
  props: {
    button_text: {
      type: String,
      default: ""
    }
  }
}
</script>


<style scoped>
.button {
  color: white;
  background: #49434B;
  cursor: pointer;
  display: flex;
  border-radius: 5px;
  align-items: center;
  justify-content: center;
  transition: 0.3s;
  width: 50%;
  height: 40px;
  margin-top: 5%;
  font-weight: bold;
}

.button:hover {
  background-color: #8521b0;
  color: white;
  cursor: pointer;
}
</style>