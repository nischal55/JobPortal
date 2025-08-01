<template>
  <div class="bg-gray-50 dark:bg-gray-900 px-6 py-20 md:px-12 lg:px-20">
    <div
      class="bg-white dark:bg-gray-800 p-8 md:p-12 shadow-sm rounded-2xl w-full max-w-md mx-auto flex flex-col gap-8"
    >
      <div class="flex flex-col items-center gap-4">
        <div class="flex items-center gap-4">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-14 w-14" viewBox="0 0 33 32" fill="none">
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M7.09219 2.87829C5.94766 3.67858 4.9127 4.62478 4.01426 5.68992C7.6857 5.34906 12.3501 5.90564 17.7655 8.61335C23.5484 11.5047 28.205 11.6025 31.4458 10.9773C31.1517 10.087 30.7815 9.23135 30.343 8.41791C26.6332 8.80919 21.8772 8.29127 16.3345 5.51998C12.8148 3.76014 9.71221 3.03521 7.09219 2.87829ZM28.1759 5.33332C25.2462 2.06 20.9887 0 16.25 0C14.8584 0 13.5081 0.177686 12.2209 0.511584C13.9643 0.987269 15.8163 1.68319 17.7655 2.65781C21.8236 4.68682 25.3271 5.34013 28.1759 5.33332ZM32.1387 14.1025C28.2235 14.8756 22.817 14.7168 16.3345 11.4755C10.274 8.44527 5.45035 8.48343 2.19712 9.20639C2.0292 9.24367 1.86523 9.28287 1.70522 9.32367C1.2793 10.25 0.939308 11.2241 0.695362 12.2356C0.955909 12.166 1.22514 12.0998 1.50293 12.0381C5.44966 11.161 11.0261 11.1991 17.7655 14.5689C23.8261 17.5991 28.6497 17.561 31.9029 16.838C32.0144 16.8133 32.1242 16.7877 32.2322 16.7613C32.2441 16.509 32.25 16.2552 32.25 16C32.25 15.358 32.2122 14.7248 32.1387 14.1025ZM31.7098 20.1378C27.8326 20.8157 22.5836 20.5555 16.3345 17.431C10.274 14.4008 5.45035 14.439 2.19712 15.1619C1.475 15.3223 0.825392 15.5178 0.252344 15.7241C0.250782 15.8158 0.25 15.9078 0.25 16C0.25 24.8366 7.41344 32 16.25 32C23.6557 32 29.8862 26.9687 31.7098 20.1378Z"
              class="fill-gray-700 dark:fill-gray-200"
            />
          </svg>
        </div>
        <div class="flex flex-col items-center gap-2 w-full">
          <div class="text-gray-900 dark:text-white text-2xl font-semibold leading-tight text-center w-full">
            Welcome Back
          </div>
          <div class="text-center w-full">
            <span class="text-gray-600 dark:text-gray-300 leading-normal">Don't have an account?</span>
            <a class="text-blue-600 font-medium ml-1 cursor-pointer hover:text-blue-700">Create today!</a>
          </div>
        </div>
      </div>

      <div class="flex flex-col gap-6 w-full">
        <div class="flex flex-col gap-2 w-full">
          <label for="email1" class="text-gray-900 dark:text-white font-medium leading-normal">Username</label>
          <InputText
            id="email1"
            type="text"
            placeholder="Username"
            v-model="email"
            class="w-full px-3 py-2 shadow-sm rounded-lg"
          />
        </div>
        <div class="flex flex-col gap-2 w-full">
          <label for="password1" class="text-gray-900 dark:text-white font-medium leading-normal">Password</label>
          <InputText
            id="password1"
            type="password"
            placeholder="Password"
            v-model="password"
            class="w-full px-3 py-2 shadow-sm rounded-lg"
          />
        </div>
        <div
          class="flex flex-col sm:flex-row items-start sm:items-center justify-end w-full gap-3 sm:gap-0"
        >
          <a class="text-blue-600 font-medium cursor-pointer hover:text-blue-700">Forgot your password?</a>
        </div>
      </div>

      <Button
        label="Sign In"
        icon="pi pi-user"
        :loading="loading"
        class="w-full py-2 rounded-lg flex justify-center items-center gap-2"
        @click="login"
      >
        <template #icon>
          <i class="pi pi-user !text-base !leading-normal" />
        </template>
      </Button>

      <p v-if="errorMsg" class="text-red-600 text-sm mt-2 text-center">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import Checkbox from 'primevue/checkbox';
import InputText from 'primevue/inputtext';
import { Button } from 'primevue';
import { ref } from 'vue';
import ApiService from '@/services/ApiService';
import { useRouter } from 'vue-router';

const router = useRouter();

const email = ref('');
const password = ref('');
const checked1 = ref(true);
const errorMsg = ref('');
const loading = ref(false);

const login = async () => {
  errorMsg.value = '';
  loading.value = true;

  try {
    const response = await ApiService.post('/users/login', {
      username: email.value,
      password: password.value,
    });

    console.log('Login Response:', response.data);

    const { id, username, role } = response.data;

    if (!id || !username || !role) {
      throw new Error("Incomplete login response");
    }

    localStorage.setItem('user_id', id);
    localStorage.setItem('username', username);
    localStorage.setItem('user_role', role);

    if (role === "admin") {
      router.push({ name: 'dashboard' });
    } else if (role === "provider") {
      router.push({ name: 'JobSeeker' });
    } else {
      router.push({ name: 'home' });
    }

  } catch (error) {
    console.error("Login error:", error);

    if (error.response?.data?.message) {
      errorMsg.value = error.response.data.message;
    } else {
      errorMsg.value = 'Login failed. Please check your credentials.';
    }
  } finally {
    loading.value = false;
  }
};



</script>

<style scoped>
/* Add any extra styling you want */
</style>
