<template>
  <div v-if="showPreview" class="preview-container">
    <div class="question-preview">
      <div class="card-content">{{ question.question }}</div>
      <div class="countdown-circle">{{ countdown }}</div>
    </div>
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{width: countdownProgress + '%'}"></div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['question'],
  data() {
    return {
      countdown: 5, // Total countdown duration
      showPreview: true,
      intervalId: null,
    };
  },
  computed: {
    countdownProgress() {
      return (this.countdown / 5) * 100; // Convert countdown to progress bar width
    },
  },
  methods: {
    startCountdown() {
      this.intervalId = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown -= 1;
        } else {
          this.endPreview();
        }
      }, 1000);
    },
    endPreview() {
      clearInterval(this.intervalId);
      this.showPreview = false;
      this.$emit('previewEnded');
    },
  },
  mounted() {
    this.startCountdown();
  },
  beforeDestroy() {
    clearInterval(this.intervalId);
  },
}
</script>

<style scoped>
.preview-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px auto;
  width: 80%;
  max-width: 600px;
}

.question-preview {
  padding: 20px 20px 80px; /* Increase padding at the bottom to make space for the countdown circle */
  background: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  width: 100%;
  position: relative;
  text-align: center;
}

.card-content {
  font-size: 1.2em;
  margin-bottom: 20px; /* Ensure there's a clear space between the text and the countdown circle */
  max-height: 100px; /* Optional: Limit the question text height */
  overflow: auto; /* Optional: Adds a scrollbar if the text exceeds the max height */
}

.countdown-circle {
  width: 60px; /* Reduce the size to decrease the likelihood of overlap */
  height: 60px;
  border-radius: 50%;
  background-color: #4caf50;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5em;
  position: absolute;
  bottom: 10px; /* Adjust the position so it does not overlap with the text */
  left: 50%;
  transform: translateX(-50%);
}

.progress-bar-container {
  width: 100%;
  background-color: #e0e0e0;
  border-radius: 5px;
  margin-top: 20px;
}

.progress-bar {
  height: 10px;
  background-color: #4caf50;
  border-radius: 5px;
}
</style>
