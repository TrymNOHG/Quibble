<template>
  <div class="submit_form">
    <h2>Register</h2>
    <form @submit.prevent="submit" :class="{ 'has-errors': has_err }">
      <div class="input_fields">
        <label for="email">Email</label>
        <input
            type="email"
            required
            v-model.trim="email"
            name="email"
            class="input-field"
            aria-labelledby="emailLabel"
            :class="{ 'error': errors && errors['email'] }"
            placeholder="Emaile"
        />
        <div v-if="errors && errors['email']" class="error-message">
          {{ errors["email"] }}
        </div>

        <label for="username">Username</label>
        <input
            type="text"
            required
            v-model.trim="username"
            name="username"
            class="input-field"
            aria-labelledby="usernameLabel"
            :class="{ 'error': errors && errors['username'] }"
            placeholder="Username"
        />
        <div v-if="errors && errors['username']" class="error-message">
          {{ $t(errors["username"]) }}
        </div>

        <label for="password">Password</label>
        <input
            type="password"
            required
            v-model.trim="passwrd"
            name="password"
            class="input-field"
            aria-labelledby="passwordLabel"
            :class="{ 'error': errors && errors['passwrd'] }"
            placeholder="Password"
        />
        <div v-if="errors && errors['passwrd']" class="error-message">
          {{ errors["passwrd"] }}
        </div>

        <label for="confirm password">Confirm Password</label>
        <input
            type="password"
            required
            v-model.trim="conf_passwrd"
            name="conf_password"
            class="input-field"
            aria-labelledby="conf_passwordLabel"
            :class="{ 'error': errors && errors['conf_passwrd'] }"
            placeholder="Confirm Passoword"
        />
        <div v-if="errors && errors['conf_passwrd']" class="error-message">
          {{ errors["conf_passwrd"] }}
        </div>

        <basic_button
            class="submit_button"
            :button_text="'Register'"
        />
      </div>
    </form>
  </div>
</template>

<script>
import * as yup from "yup";
import { useField, useForm } from "vee-validate";
import Basic_button from "@/components/BasicComponents/basic_button.vue";
import { ref } from "vue";
import router from "@/router/index.js";
import {useUserStore} from "@/stores/counter.js";

export default {
  components: {Basic_button},

  setup() {
    const store = useUserStore();
    const submitMessage = ref("");

    const validationSchema = yup.object({
      email: yup.string().required("Email is required").email("Must be an valid email"),
      username: yup.string().required("Username is required"),
      passwrd: yup.string().required("Password is required").min(8, "Password must be at least 8 characters"),
      conf_passwrd: yup.string()
          .required("Confirm password")
          .oneOf([yup.ref('passwrd'), null], 'Passwords must match')
    });

    const {handleSubmit, errors, setFieldTouched, setFieldValue} = useForm({
      validationSchema,
      initialValues: {
        email: "",
        username: "",
        passwrd: "",
        conf_passwrd: "",
      },
    });

    const {value: email} = useField("email")
    const {value: username} = useField("username");
    const {value: passwrd} = useField("passwrd");
    const {value: conf_passwrd} = useField("conf_passwrd");


    const submit = handleSubmit(async () => {
      const userData = {
        email: email.value,
        user: username.value,
        passwrd: passwrd.value,
        conf_passwrd: conf_passwrd.value
      };

      await registerUser(userData)
        .then(async (response) => {
          if (response !== undefined) {
            store.setToken(response.data.token);
            await store.fetchUserData();
            await router.push("/fridges?appTour=true");
          }
        })
        .catch((error) => {
          submitMessage.value = "register_error";
          setTimeout(() => {
            submitMessage.value = "";
          }, 2000);
          console.warn("error", error);
        });

    });

    const has_err = () => {
      const keys = Object.keys(errors.value);
      return keys.length > 0;
    };

    return {
      email,
      username,
      passwrd,
      conf_passwrd,
      errors,
      submit,
      validationSchema,
      has_err,
      setFieldTouched,
      setFieldValue,
    };
  }
}
</script>

<style scoped>
h2 {
  font-weight: bold;
}

.input_fields {
  gap: 10px;
  display: flex;
  flex-direction: column;
  text-align: left;
  align-items: center;
  justify-content: center;
  margin: 25px 10px 10px 20px;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  font-size: 16px;
}

.submit_form {
  padding: 40px;
  width: 20%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: transparent;
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.25);
  margin-top: 2.5%;
}

.submit_button {
  margin-top: 20px;
  width: 100%;
}

.has-errors input.error {
  border-color: red;
}

.error-message {
  font-size: 12px;
  color: red;
}

@media only screen and (max-width: 428px) {
  .submit_form {
    padding: 10px;
    width: 90%;
    margin-top: 25%;
  }
}
</style>
