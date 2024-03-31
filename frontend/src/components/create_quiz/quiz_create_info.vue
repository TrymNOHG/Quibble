<template>
  <div class="quiz">
    <h1 class="header_txt">{{ $t('quiz_details.header_txt') }}</h1>
    <div class="quiz-info">
      <div class="img">
        <input class="img_input" type="file" @change="handleImageUpload">
        <div class="image_container">
          <img v-if="template_quiz.Image" :src="template_quiz.Image" :alt="$t('quiz_details.image_label')" class="uploaded-image">
        </div>
        <div>
          <h2>{{ $t('quiz_details.image_label') }}</h2>
          <h4>{{ $t('quiz_details.image_best_resolution') }}</h4>
        </div>
      </div>
      <div class="quiz-details">
        <label for="quizName" class="quiz-label">{{ $t('quiz_details.name_label') }}</label>
        <input class="input-area" type="text" id="quizName" v-model="template_quiz.Name">
        <label for="difficulty" class="quiz-label">{{ $t('quiz_details.difficulty_label') }}</label>
        <select class="input-area" id="difficulty" v-model="template_quiz.Difficulty">
          <option value="Easy">{{ $t('dropdown_options.EASY') }}</option>
          <option value="Medium">{{ $t('dropdown_options.MEDIUM') }}</option>
          <option value="Hard">{{ $t('dropdown_options.HARD') }}</option>
        </select>
        <label for="description" class="quiz-label">{{ $t('quiz_details.description_label') }}</label>
        <textarea class="input-area" id="description" v-model="template_quiz.Description"></textarea>
      </div>
    </div>
    <div class="tags">
      <div class="header">
        <h2>{{ $t('quiz_details.tags_categories_header') }}</h2>
        <font-awesome-icon
            id="add"
            icon="fa-solid fa-circle-plus"
            @click="showPopup = true"
        />
      </div>
      <tag_list
          v-for="(t, index) in template_tags.tags"
          :key="index"
          :tag="t"
          @deleteTag="deleteTag(t)"
      />
      <div class="popup" v-if="showPopup">
        <div class="popup-content">
          <h3>{{ $t('quiz_details.add_tag_category') }}</h3>
          <div class="input-group">
            <span class="add-span">{{ $t('quiz_details.tag_label') }}</span>
            <input type="text" v-model="newTag.tag_desc"/>
            <span class="add-span">{{ $t('quiz_details.type_label') }}</span>
            <select class="add-area" v-model="newTag.type">
              <option value="Category">{{ $t('dropdown_options.CATEGORY') }}</option>
              <option value="Keyword">{{ $t('dropdown_options.KEYWORD') }}</option>
            </select>
          </div>
          <div class="button-group">
            <button @click="addTag">{{ $t('quiz_details.add_button') }}</button>
            <button @click="closePopup">{{ $t('quiz_details.cancel_button') }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import Tag_list from "@/components/create_quiz/tag_list.vue";
import { useQuizCreateStore } from "@/stores/counter.js";

export default {
  components: { Tag_list },

  setup() {
    const showPopup = ref(false);
    const store = useQuizCreateStore();
    const template_quiz = ref(store.templateQuiz);
    const template_tags = ref(store.template_tags);

    const newTag = ref({
      tag_desc: '',
      type: 'Category'
    });

    const handleImageUpload = (event) => {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = () => {
          template_quiz.value.Image = reader.result;
        };
        reader.readAsDataURL(file);
      }
    };

    const addTag = () => {
      if (newTag.value.tag_desc.trim() !== '') {
        for (const tag of template_tags.value.tags) {
          if (tag.tag_desc === newTag.value.tag_desc) {
            console.log("Tag already exists");
            return;
          }
        }

        template_tags.value.tags.push({ ...newTag.value });
        newTag.value.tag_desc = '';
        newTag.value.type = 'Category';
        showPopup.value = false;
      }
    };

    const deleteTag = (tagToDelete) => {
      const index = template_tags.value.tags.findIndex(tag => tag.tag_desc === tagToDelete.tag_desc);
      if (index !== -1) {
        template_tags.value.tags.splice(index, 1);
      }
    };

    const closePopup = () => {
      newTag.value.tag_desc = '';
      newTag.value.type = 'Category';
      showPopup.value = false;
    };

    return {
      template_quiz,
      template_tags,
      handleImageUpload,
      addTag,
      deleteTag,
      closePopup,
      showPopup,
      newTag
    };
  }
}
</script>

<style scoped>
.input-area {
  height: 40px;
  width: 80%;
}

.quiz-label {
  margin-top: 5%;
  font-size: 18px;
  font-weight: bold;
}

.img_input {
  display: block;
  padding: 10px 15px;
  background-color: #4991fa;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.image_container {
  margin-top: 10px;
  background-color: rgba(205, 205, 205, 0.96);
  border-radius: 10px;
  height: 200px;
  width: 320px;
  margin-right: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.uploaded-image {
  width: 100%;
  height: 100%;
  border-radius: 10px;
}

button {
  width: 15%;
  height: 35px;
  color: white;
  text-align: center;
  margin-bottom: 3%;
  justify-content: space-between;
  align-content: center;
  background-color: #b22fe8;
  border-radius: 5px;
  font-size: 14px;
}

.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.popup-content {
  display: flex;
  flex-direction: column;
  background-color: #fefefe;
  margin: 20% auto;
  padding: 20px;
  border-radius: 5px;
  width: 20%;
}

.input-group {
  margin-bottom: 10px;
}

.input-group input {
  width: 100%;
  height: 25px;
}

.input-group select {
  width: 100%;
  height: 25px;
}

.button-group {
  display: flex;
  justify-content: space-evenly;
  margin-top: 10px;
}

.button-group button {
  width: 100px;
}

.header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-content: center;
  align-items: center;
}

#add:hover {
  scale: 2.2;
  cursor: pointer;
  color: #71da11;
}

#add {
  scale: 2;
}

.quiz {
  display: grid;
  grid-template-rows: 1fr 1fr;
  padding: 20px;
  background-color: #fff;
  border-width: 0 1px 0 0;
  border-style: solid;
  border-color: #ccc;
}

.quiz-info {
  border-width: 0 0 1px 0;
  border-style: solid;
  border-color: #ccc;
}

.quiz-details {
  margin-top: 5%;
  display: flex;
  flex-direction: column;
}

.tags {
  margin-top: 10px;
}

.input-group select::-ms-expand {
  display: none;
}

@media only screen and (max-width: 428px) {
  .popup-content {
    width: 80%;
  }
}
</style>
