<template>
  <input
      v-bind="$attrs"
      :value="modelValue"
      :placeholder="label"
      @input="$emit('update:modelValue', $event.target.value)"
      class="field"
      :style="{ borderColor: error ? 'red' : ''}"
      :aria-invalid='error ? true : null'
  >
  <p
      v-if="error"
      class="errorMessage"
      aria-live="assertive"
  >
    {{ error }}
  </p>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
      default: ''
    },
    modelValue: {
      type: [String, Number],
      default: ''
    },
    error: {
      type: String,
      default: ''
    }
  }
}
</script>

<style>

.field {
  color: black;
  background-color: white;
  border: 2px solid #ccc;
  padding: 10px;
  width: 100%;
  display: flex;
  align-items: center;
  align-content: center;
  line-height: 2;
  ruby-align: space-between;
  border-radius: 5px;
  text-align: center;
}

input {
  display: flex;
  align-items: center;
  align-content: center;
  padding: 5%;
  width: 100%;
  border-radius: 5px;
  text-align: center;
}

p {
  color: red;
  font-size: 10px;
}
</style>