<template>
  <nav class="flex items-center justify-between px-8 py-2 bg-white shadow">
    <!-- Left side: Logo only -->
    <div class="text-xl font-bold text-gray-800">
      JobPortal
    </div>

    <!-- Right side: Menu + Profile -->
    <div class="flex items-center space-x-8">
      <!-- Desktop menu -->
      <div class="hidden md:flex items-center space-x-8">
        <router-link to="/home" class="text-gray-700 hover:text-gray-900">Home</router-link>
        <router-link to="/jobs" class="text-gray-700 hover:text-gray-900">Jobs</router-link>
        <router-link to="/contact" class="text-gray-700 hover:text-gray-900">Contact</router-link>
      </div>

      <!-- Profile dropdown -->
      <div class="relative" @click="toggleDropdown">
        <img
          src="https://randomuser.me/api/portraits/men/32.jpg"
          alt="Profile"
          class="w-10 h-10 rounded-full cursor-pointer"
        />
        <div
          v-if="isDropdownOpen"
          class="absolute right-0 z-50 mt-2 w-48 bg-white border border-gray-200 rounded shadow-md"
        >
          <button
            @click="logout"
            class="block w-full px-4 py-2 text-left text-red-600 hover:bg-gray-100"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  </nav>

  <!-- Mobile menu -->
  <div v-if="isMobileMenuOpen" class="md:hidden px-8 py-2 bg-white shadow">
    <router-link to="/home" class="block py-2 text-gray-700 hover:bg-gray-100">Home</router-link>
    <router-link to="/jobs" class="block py-2 text-gray-700 hover:bg-gray-100">Jobs</router-link>
    <router-link to="/contact" class="block py-2 text-gray-700 hover:bg-gray-100">Contact</router-link>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isDropdownOpen = ref(false)
const isMobileMenuOpen = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// Optional if you add hamburger menu later
const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

const logout = () => {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  router.push('/user-login')
}
</script>

<style scoped>
/* Make sure no weird global styles interfere */
</style>
