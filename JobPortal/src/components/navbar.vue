<template>
  <nav class="bg-surface-0 dark:bg-surface-900 shadow-sm px-4 py-3">
    <div class="max-w-7xl mx-auto flex items-center justify-between">
      <!-- Brand -->
      <div class="text-xl font-semibold text-primary-600 dark:text-primary-300">
        JobPortal
      </div>

      <!-- Desktop Nav Links -->
      <div class="hidden md:flex items-center space-x-6">
        <a href="#" class="text-sm text-surface-700 dark:text-surface-200 hover:text-primary-600">Home</a>
        <a href="#" class="text-sm text-surface-700 dark:text-surface-200 hover:text-primary-600">Jobs</a>
        <a href="#" class="text-sm text-surface-700 dark:text-surface-200 hover:text-primary-600">Contact</a>

        <!-- Profile Dropdown -->
        <div class="relative" @click="toggleDropdown">
          <img
            src="https://i.pravatar.cc/32"
            class="w-8 h-8 rounded-full cursor-pointer border border-surface-200 dark:border-surface-600"
            alt="User Avatar"
          />
          <Transition
            enter-active-class="transition duration-200 ease-out"
            enter-from-class="opacity-0 translate-y-2"
            enter-to-class="opacity-100 translate-y-0"
            leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100 translate-y-0"
            leave-to-class="opacity-0 translate-y-2"
          >
            <div
              v-if="dropdownOpen"
              class="absolute z-10 right-0 mt-2 w-48 bg-surface-0 dark:bg-surface-800 border border-surface-200 dark:border-surface-700 rounded-md shadow-lg z-50"
            >
              <a href="#" class="block px-4 py-2 text-sm text-surface-700 dark:text-surface-200 hover:bg-surface-100 dark:hover:bg-surface-700">My Profile</a>
              <a href="#" class="block px-4 py-2 text-sm text-surface-700 dark:text-surface-200 hover:bg-surface-100 dark:hover:bg-surface-700">Settings</a>
              <a href="#" class="block px-4 py-2 text-sm text-red-600 hover:bg-red-50 dark:hover:bg-red-600 dark:hover:text-white">Logout</a>
            </div>
          </Transition>
        </div>
      </div>

      <!-- Mobile Menu Button -->
      <button @click="isOpen = !isOpen" class="md:hidden text-surface-700 dark:text-surface-200 transition-all">
        <i :class="['pi', isOpen ? 'pi-times' : 'pi-bars', 'text-xl']"></i>
      </button>
    </div>

    <!-- Mobile Nav Menu -->
    <Transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="opacity-0 -translate-y-4"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 -translate-y-4"
    >
      <div v-if="isOpen" class="md:hidden mt-3 space-y-2 px-4">
        <a href="#" class="block text-surface-700 dark:text-surface-200 hover:text-primary-600">Home</a>
        <a href="#" class="block text-surface-700 dark:text-surface-200 hover:text-primary-600">Jobs</a>
        <a href="#" class="block text-surface-700 dark:text-surface-200 hover:text-primary-600">Contact</a>
        <a href="#" class="block text-surface-700 dark:text-surface-200 hover:text-primary-600">My Profile</a>
        <a href="#" class="block text-surface-700 dark:text-surface-200 hover:text-primary-600">Settings</a>
        <a href="#" class="block text-red-600 hover:text-red-700 dark:hover:text-red-400">Logout</a>
      </div>
    </Transition>
  </nav>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

const isOpen = ref(false);
const dropdownOpen = ref(false);

const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
};

// Close dropdown when clicking outside
const handleClickOutside = (e) => {
  const dropdown = document.querySelector('.relative');
  if (dropdown && !dropdown.contains(e.target)) {
    dropdownOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>
