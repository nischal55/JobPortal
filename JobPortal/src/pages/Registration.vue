<template>
  <div class="bg-gray-50 dark:bg-gray-900 px-6 py-20 md:px-12 lg:px-20">
    <div class="bg-white dark:bg-gray-800 p-8 md:p-12 shadow-sm rounded-2xl w-full max-w-md mx-auto flex flex-col gap-8">
      <!-- Header -->
      <div class="flex flex-col items-center gap-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-14 w-14" viewBox="0 0 33 32" fill="none">
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M7.09219 2.87829C5.94766 3.67858 4.9127 4.62478 4.01426 5.68992C7.6857 5.34906 12.3501 5.90564 17.7655 8.61335C23.5484 11.5047 28.205 11.6025 31.4458 10.9773C31.1517 10.087 30.7815 9.23135 30.343 8.41791C26.6332 8.80919 21.8772 8.29127 16.3345 5.51998C12.8148 3.76014 9.71221 3.03521 7.09219 2.87829ZM28.1759 5.33332C25.2462 2.06 20.9887 0 16.25 0C14.8584 0 13.5081 0.177686 12.2209 0.511584C13.9643 0.987269 15.8163 1.68319 17.7655 2.65781C21.8236 4.68682 25.3271 5.34013 28.1759 5.33332ZM32.1387 14.1025C28.2235 14.8756 22.817 14.7168 16.3345 11.4755C10.274 8.44527 5.45035 8.48343 2.19712 9.20639C2.0292 9.24367 1.86523 9.28287 1.70522 9.32367C1.2793 10.25 0.939308 11.2241 0.695362 12.2356C0.955909 12.166 1.22514 12.0998 1.50293 12.0381C5.44966 11.161 11.0261 11.1991 17.7655 14.5689C23.8261 17.5991 28.6497 17.561 31.9029 16.838C32.0144 16.8133 32.1242 16.7877 32.2322 16.7613C32.2441 16.509 32.25 16.2552 32.25 16C32.25 15.358 32.2122 14.7248 32.1387 14.1025ZM31.7098 20.1378C27.8326 20.8157 22.5836 20.5555 16.3345 17.431C10.274 14.4008 5.45035 14.439 2.19712 15.1619C1.475 15.3223 0.825392 15.5178 0.252344 15.7241C0.250782 15.8158 0.25 15.9078 0.25 16C0.25 24.8366 7.41344 32 16.25 32C23.6557 32 29.8862 26.9687 31.7098 20.1378Z"
            class="fill-gray-700 dark:fill-gray-200"
          />
        </svg>
        <div class="text-gray-900 dark:text-white text-2xl font-semibold leading-tight text-center w-full">
          Create an Account
        </div>
        <div class="text-center w-full">
          <span class="text-gray-600 dark:text-gray-300 leading-normal">Already registered?</span>
          <a class="text-blue-600 font-medium ml-1 cursor-pointer hover:text-blue-700" @click="router.push({ name: 'login' })">Login now!</a>
        </div>
      </div>

      <!-- Form Inputs -->
      <div class="flex flex-col gap-6 w-full">
        <div class="flex flex-col gap-2 w-full">
          <label for="username" class="text-gray-900 dark:text-white font-medium leading-normal">Username</label>
          <InputText v-model="username" id="username" placeholder="Enter your username" class="w-full px-3 py-2 shadow-sm rounded-lg" />
        </div>

        <div class="flex flex-col gap-2 w-full">
          <label for="password" class="text-gray-900 dark:text-white font-medium leading-normal">Password</label>
          <InputText v-model="password" id="password" type="password" placeholder="Choose a password" class="w-full px-3 py-2 shadow-sm rounded-lg" />
        </div>

        <div class="flex flex-col gap-2 w-full">
          <label for="confirmPassword" class="text-gray-900 dark:text-white font-medium leading-normal">Confirm Password</label>
          <InputText v-model="confirmPassword" id="confirmPassword" type="password" placeholder="Confirm your password" class="w-full px-3 py-2 shadow-sm rounded-lg" />
        </div>

        <div class="flex flex-col gap-2 w-full">
          <label for="role" class="text-gray-900 dark:text-white font-medium leading-normal">Role</label>
          <Dropdown
            v-model="role"
            :options="roles"
            option-label="label"
            option-value="value"
            placeholder="Select role"
            class="w-full shadow-sm rounded-lg"
          />
        </div>
      </div>

      <!-- Submit Button -->
      <Button
        label="Register"
        icon="pi pi-user-plus"
        :loading="loading"
        class="w-full py-2 rounded-lg flex justify-center items-center gap-2"
        @click="register"
      >
        <template #icon>
          <i class="pi pi-user-plus !text-base !leading-normal" />
        </template>
      </Button>

      <!-- Feedback -->
      <p v-if="errorMsg" class="text-red-600 text-sm mt-2 text-center">{{ errorMsg }}</p>
      <p v-if="successMsg" class="text-green-600 text-sm mt-2 text-center">{{ successMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import { Button } from 'primevue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ApiService from '@/services/ApiService';

const router = useRouter();

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const role = ref(null);
const loading = ref(false);
const errorMsg = ref('');
const successMsg = ref('');

const roles = [
  { label: 'Seeker', value: 'seeker' },
  { label: 'Provider', value: 'provider' },
];

const register = async () => {
  errorMsg.value = '';
  successMsg.value = '';

  // Password match validation
  if (password.value !== confirmPassword.value) {
    errorMsg.value = 'Passwords do not match.';
    return;
  }

  loading.value = true;

  try {
    const response = await ApiService.post('/users/create', {
      username: username.value,
      password: password.value,
      role: role.value,
    });

    console.log('Registration Response:', response.data);
    successMsg.value = 'Registration successful! Redirecting to login...';
    setTimeout(() => router.push({ name: 'jobseekerAccountCreation' }), 2000);
  } catch (error) {
    console.error('Registration error:', error);
    errorMsg.value = error.response?.data?.message || 'Registration failed.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* Additional styling (if needed) */
</style>
