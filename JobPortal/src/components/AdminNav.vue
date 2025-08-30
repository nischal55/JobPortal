<template>
  <nav class="h-14 bg-white dark:bg-gray-900 shadow flex items-center justify-end px-6">


    <!-- Profile dropdown -->
    <div class="relative">
      <button id="profile-button" @click="toggleProfileDropdown"
        class="flex items-center gap-2 focus:outline-none cursor-pointer" aria-haspopup="true"
        :aria-expanded="isProfileOpen.toString()">
        <img src="https://i.pravatar.cc/40" alt="Profile" class="w-8 h-8 rounded-full border-2 border-emerald-600" />
        <span class="hidden sm:inline text-gray-700 dark:text-gray-200 font-medium">
          Admin User
        </span>
        <i :class="['pi', isProfileOpen ? 'pi-angle-up' : 'pi-angle-down']"
          class="text-gray-700 dark:text-gray-200"></i>
      </button>

      <!-- Dropdown -->
      <div v-show="isProfileOpen" id="profile-dropdown"
        class="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-md shadow-lg py-2 z-50 cursor-pointer">
        <button class="block w-full px-4 py-2 text-left text-slate-600 hover:bg-gray-100 cursor-pointer">
          My Profile
        </button>
        <button @click="logout" class="block w-full px-4 py-2 text-left text-red-600 hover:bg-gray-100 cursor-pointer">
          Logout
        </button>
      </div>
    </div>
  </nav>

</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const isOpen = ref(true)
const isProfileOpen = ref(false)

function toggleSidebar() {
  isOpen.value = !isOpen.value
}

const logout = () => {
  localStorage.removeItem('user_role')
  localStorage.removeItem('username')
  localStorage.removeItem('user_id')
  router.push('/userLogin')
}

function toggleProfileDropdown() {
  isProfileOpen.value = !isProfileOpen.value
}

function onClickOutside(event) {
  const dropdown = document.getElementById('profile-dropdown')
  const button = document.getElementById('profile-button')

  if (
    dropdown &&
    button &&
    !dropdown.contains(event.target) &&
    !button.contains(event.target)
  ) {
    isProfileOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', onClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', onClickOutside)
})
</script>

<style scoped>
aside {
  transition-property: width;
  transition-duration: 300ms;
}
</style>